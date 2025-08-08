package study.msa.gatewayreactive.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private val logger = KotlinLogging.logger {}

@Component
class AuthorizationHeaderFilter(
    var env: Environment
): AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>(Config::class.java) {
    class Config

    override fun apply(config: AuthorizationHeaderFilter.Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request: ServerHttpRequest = exchange.request
            if (!request.headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return@GatewayFilter onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED)
            }

            val authorizationHeader: String = request.headers[HttpHeaders.AUTHORIZATION]!![0]
            val jwt = authorizationHeader.replace("Bearer ", "")

            if (!isJwtValid(jwt)) {
                return@GatewayFilter onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED)
            }

            chain.filter(exchange)
        }
    }

    private fun onError(exchange: ServerWebExchange, err: String, httpStatus: HttpStatus): Mono<Void?> {
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(httpStatus)
        logger.error { err }

        val bytes: ByteArray = "The requested token is invalid.".toByteArray(StandardCharsets.UTF_8)
        val buffer: DataBuffer = exchange.response.bufferFactory().wrap(bytes)
        return response.writeWith(Flux.just(buffer))
    }

    private fun isJwtValid(jwt: String): Boolean {
        val secretKeyBytes = env.getProperty("token.secret")!!.toByteArray()
        val signingKey: SecretKey = SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.jcaName)

        var returnValue = true
        var subject: String? = null

        try {
            val jwtParser: JwtParser = Jwts.parser()
                .setSigningKey(signingKey)
                .build()

            subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject()
        } catch (ex: Exception) {
            returnValue = false
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false
        }

        return returnValue
    }
}
