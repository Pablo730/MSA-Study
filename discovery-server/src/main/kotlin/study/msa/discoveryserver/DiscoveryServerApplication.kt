package study.msa.discoveryserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import study.msa.discoveryserver.config.DotenvLoader

@SpringBootApplication
@EnableEurekaServer
@ConfigurationPropertiesScan(basePackages = ["study.msa.discoveryserver.config.properties"])
class DiscoveryServerApplication

fun main(args: Array<String>) {
	DotenvLoader.load()

	runApplication<DiscoveryServerApplication>(*args)
}
