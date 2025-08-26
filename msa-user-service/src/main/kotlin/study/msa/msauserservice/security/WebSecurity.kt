package study.msa.msauserservice.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager
import org.springframework.security.web.util.matcher.IpAddressMatcher
import study.msa.msauserservice.user.service.UserService


@Configuration
@EnableWebSecurity
class WebSecurity(
    private val userService: UserService,
    private val env: Environment,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @Bean
    @Throws(Exception::class)
    protected fun configure(http: HttpSecurity): SecurityFilterChain {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder)

        val authenticationManager = authenticationManagerBuilder.build()

        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .authorizeHttpRequests(
                { auth -> auth
                    .requestMatchers("/actuator/**").permitAll() // 특정 경로 허용
                    .requestMatchers("/health-check/**").permitAll() // 특정 경로 허용
                    .anyRequest().access { _, context ->
                        val request = context.request
                        // 요청 헤더에 X-MSA-REQUEST가 있고, 그 값이 gateway 비밀키와 일치하는지 확인
                        val isFromGateway = request.getHeader("X-MSA-REQUEST") == env.getProperty("gateway.secret")
                        AuthorizationDecision(isFromGateway)
                    }
                }
            )
            .authenticationManager(authenticationManager)
            .addFilter(getAuthenticationFilter(authenticationManager))
            .httpBasic(Customizer.withDefaults()) // ← Basic 인증 추가
            .headers { headers: HeadersConfigurer<HttpSecurity> -> headers.frameOptions( { frameOptions -> frameOptions.sameOrigin() }) }

        return http.build()
    }

    @Throws(java.lang.Exception::class)
    private fun getAuthenticationFilter(authenticationManager: AuthenticationManager): AuthenticationFilter {
        val authenticationFilter = AuthenticationFilter(userService, env, authenticationManager)
        authenticationFilter.setAuthenticationManager(authenticationManager)

        return authenticationFilter
    }

    companion object{
        val ALLOWED_IP_ADDRESS: String = "127.0.0.1"
        val SUBNET: String = "/32"
        val ALLOWED_IP_ADDRESS_MATCHER: IpAddressMatcher = IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET)
    }
}
