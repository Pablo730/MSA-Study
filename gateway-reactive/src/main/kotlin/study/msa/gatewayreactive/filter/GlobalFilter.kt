package study.msa.gatewayreactive.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@Component
class GlobalFilter : AbstractGatewayFilterFactory<GlobalFilter.Config>(Config::class.java) {
    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request: ServerHttpRequest = exchange.request
            val response: ServerHttpResponse = exchange.response

            val baseMassage = config.baseMessage
            val requestRemoteAddress = request.remoteAddress
            logger.info { "Global Filter baseMessage: $baseMassage, $requestRemoteAddress" }

            if (config.preLogger) {
                val requestId = request.id
                logger.info { "Global Filter Start: request id -> $requestId" }
            }
            chain.filter(exchange).then<Void>(Mono.fromRunnable<Void> {
                if (config.postLogger) {
                    val responseStatusCode = response.statusCode
                    logger.info { "Global Filter End: response code -> $responseStatusCode" }
                }
            })
        }
    }

    class Config (
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger: Boolean
    ) {
    }
}
