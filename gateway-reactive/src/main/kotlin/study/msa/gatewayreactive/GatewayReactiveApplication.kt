package study.msa.gatewayreactive

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableDiscoveryClient
class GatewayReactiveApplication {
	@Bean
	fun httpExchangeRepository(): HttpExchangeRepository {
		return InMemoryHttpExchangeRepository()
	}
}

fun main(args: Array<String>) {
	runApplication<GatewayReactiveApplication>(*args)
}
