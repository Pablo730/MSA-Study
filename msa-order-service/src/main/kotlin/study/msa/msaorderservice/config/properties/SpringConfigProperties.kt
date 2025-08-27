package study.msa.msaorderservice.config.properties

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated


@Validated
@ConfigurationProperties(prefix = "spring.cloud.config")
data class SpringConfigProperties(
    val discovery: Discovery,

    @field:NotBlank(message = "config 서버 name이 비어있을 수 없습니다.")
    val name: String,

    @field:NotBlank(message = "config 서버 username이 비어있을 수 없습니다.")
    val username: String,

    @field:NotBlank(message = "config 서버 password가 비어있을 수 없습니다.")
    val password: String
) {
    /**
     * spring.cloud.config.discovery 하위의 프로퍼티들을 매핑하기 위한 중첩 클래스
     */
    @Validated
    data class Discovery(
        @field:NotBlank(message = "config 서버의 Service ID가 비어있을 수 없습니다.")
        val serviceId: String
    )
}
