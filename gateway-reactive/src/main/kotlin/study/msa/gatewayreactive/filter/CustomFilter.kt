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
class CustomFilter : AbstractGatewayFilterFactory<CustomFilter.Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request: ServerHttpRequest = exchange.request
            val response: ServerHttpResponse = exchange.response

            // Custom Pre Filter
            val requestId = request.id
            logger.info { "Custom PRE Filter: request id -> $requestId" }
            chain.filter(exchange).then(Mono.fromRunnable {
                // Custom Post Filter
                val responseStatusCode = response.statusCode
                logger.info { "Custom POST Filter: response code -> $responseStatusCode" }
            })
        }
    }

    class Config
}
