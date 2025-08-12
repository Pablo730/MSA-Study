package study.msa.msauserservice.client.error

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class FeignErrorDecoder(
    private val env: Environment
) : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        when (response.status()) {
            400 -> {}
            404 -> if (methodKey.contains("getOrders")) {
                return ResponseStatusException(
                    HttpStatus.valueOf(response.status()),
                    // "User's orders is empty"
                    env.getProperty("order-service.exception.order-is-empty")
                )
            }
            else -> return Exception(response.reason())
        }
        return Exception(response.reason())
    }
}
