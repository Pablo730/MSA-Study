package study.msa.configservice.config.properties

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated @ConfigurationProperties(prefix = "spring.cloud.config.server.git")
data class GitProperties(
    @field:URL(message = "Git URI 형식이 올바르지 않습니다.")
    val uri: String,

    @field:NotBlank(message = "Git 기본 브랜치(label)는 비어있을 수 없습니다.")
    val defaultLabel: String
)
