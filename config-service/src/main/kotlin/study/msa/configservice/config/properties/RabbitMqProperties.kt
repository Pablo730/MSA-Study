package study.msa.configservice.config.properties

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated @ConfigurationProperties(prefix = "spring.rabbitmq")
data class RabbitMqProperties(
    @field:Pattern(
        regexp = "^(rabbitmq|localhost|(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)))$",
        message = "RabbitMQ 호스트는 'rabbitmq', 'localhost' 또는 올바른 IP 주소 형식이어야 합니다."
    )
    val host: String,

    @field:Min(1024, message = "포트 번호는 1024 이상이어야 합니다.")
    @field:Max(65535, message = "포트 번호는 65535 이하여야 합니다.")
    val port: Int,

    @field:NotBlank(message = "RabbitMQ 사용자 이름은 비어있을 수 없습니다.")
    val username: String,

    @field:NotBlank(message = "RabbitMQ 비밀번호는 비어있을 수 없습니다.")
    val password: String
)
