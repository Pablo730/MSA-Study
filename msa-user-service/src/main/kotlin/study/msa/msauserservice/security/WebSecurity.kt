package study.msa.msauserservice.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.IpAddressMatcher
import study.msa.msauserservice.user.service.UserService


@Configuration
@EnableWebSecurity
class WebSecurity(
    private val userService: UserService,
    private val env: Environment
) {
    @Bean
    @Throws(Exception::class)
    protected fun configure(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .authorizeHttpRequests {
                auth -> auth
                    .requestMatchers("/h2-console/**").permitAll() // 특정 경로 허용
                    .anyRequest().authenticated()
            } // 그 외는 인증 필요
            .httpBasic(Customizer.withDefaults()) // ← Basic 인증 추가
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.sameOrigin() } }

        return http.build()
    }

    companion object{
        val ALLOWED_IP_ADDRESS: String = "127.0.0.1"
        val SUBNET: String = "/32"
        val ALLOWED_IP_ADDRESS_MATCHER: IpAddressMatcher = IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET)
    }
}
