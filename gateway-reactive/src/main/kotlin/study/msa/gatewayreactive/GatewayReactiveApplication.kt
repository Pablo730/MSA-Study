package study.msa.gatewayreactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class GatewayReactiveApplication

fun main(args: Array<String>) {
	runApplication<GatewayReactiveApplication>(*args)
}
