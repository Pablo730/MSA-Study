package study.msa.gatewayreactive

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import study.msa.gatewayreactive.config.DotenvLoader


@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["study.msa.gatewayreactive.config.properties"])
class GatewayReactiveApplication {
	@Bean
	fun httpExchangeRepository(): HttpExchangeRepository {
		return InMemoryHttpExchangeRepository()
	}
}

fun main(args: Array<String>) {
	DotenvLoader.load()

	runApplication<GatewayReactiveApplication>(*args)
}
