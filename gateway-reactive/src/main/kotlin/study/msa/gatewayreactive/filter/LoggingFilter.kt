package study.msa.gatewayreactive.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@Component
class LoggingFilter : AbstractGatewayFilterFactory<LoggingFilter.Config>(Config::class.java) {
    /* 우선 순위를 갖는 Logging Filter 적용 */
    override fun apply(config: Config): GatewayFilter {
        return OrderedGatewayFilter({ exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request: ServerHttpRequest = exchange.request
            val response: ServerHttpResponse = exchange.response

            val baseMassage = config.baseMessage
            val requestRemoteAddress = request.remoteAddress

            logger.info { "Logging Filter baseMessage: $baseMassage, $requestRemoteAddress" }

            if (config.preLogger) {
                val requestUri = request.uri.toString()
                logger.info { "Logging Filter Start: request uri -> $requestUri" }
            }
            chain.filter(exchange).then(Mono.fromRunnable {
                if (config.postLogger) {
                    val responseStatusCode = response.statusCode
                    logger.info { "Logging Filter End: response code -> $responseStatusCode" }
                }
            })
        }, OrderedGatewayFilter.HIGHEST_PRECEDENCE)
    }

    class Config (
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger: Boolean
    )
}
