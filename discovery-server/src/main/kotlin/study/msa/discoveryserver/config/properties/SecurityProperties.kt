package study.msa.discoveryserver.config.properties

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "spring.security.user")
data class SecurityProperties (
    @field:NotBlank(message = "Security User Name이 비어있을 수 없습니다.")
    val name: String,

    @field:NotBlank(message = "Security User Password가 비어있을 수 없습니다.")
    val password: String
)
