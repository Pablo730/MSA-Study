package study.msa.gatewayreactive.config.properties

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated


@Validated
@ConfigurationProperties(prefix = "spring.profiles")
data class ProfilesProperties(
    @field:NotBlank(message = "프로필 acitve가 비어있을 수 없습니다.")
    val active: String,
)
