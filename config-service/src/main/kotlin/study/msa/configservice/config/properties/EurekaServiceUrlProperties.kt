package study.msa.configservice.config.properties

import org.hibernate.validator.constraints.URL
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated


@Validated
@ConfigurationProperties(prefix = "eureka.client.service-url")
data class EurekaServiceUrlProperties (
    @field:URL(message = "Eureka Discovery URL 형식이 올바르지 않습니다.")
    val defaultZone: String,
)

