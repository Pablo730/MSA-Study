package study.msa.configservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // csrf 비활성화
            .authorizeHttpRequests { it.anyRequest().authenticated() } // 모든 요청에 대해 인증을 요구하도록 설정
            .httpBasic { } // HTTP Basic Authentication 활성화
            .formLogin { } // 웹 브라우저를 위한 폼 로그인 지원

        return http.build()
    }
}
