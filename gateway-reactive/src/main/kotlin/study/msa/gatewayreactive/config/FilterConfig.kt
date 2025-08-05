package study.msa.gatewayreactive.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
//    @Bean
    fun getRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route { r: PredicateSpec ->
                r.path("/first-service/**")
                    .filters { f: GatewayFilterSpec ->
                        f.addRequestHeader("f-request", "1st-request-header-by-kotlin")
                            .addResponseHeader("f-response", "1st-response-header-from-kotlin")
                    }
                    .uri("http://localhost:8081")
            }
            .route { r: PredicateSpec ->
                r.path("/second-service/**")
                    .filters { f: GatewayFilterSpec ->
                        f.addRequestHeader("s-request", "2nd-request-header-by-kotlin")
                            .addResponseHeader("s-response", "2nd-response-header-from-kotlin")
                    }
                    .uri("http://localhost:8082")
            }
            .build()
    }
}
