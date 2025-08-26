package study.msa.msauserservice

import feign.Logger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.client.RestTemplate
import study.msa.msauserservice.config.DotenvLoader
import javax.sql.DataSource


@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = ["study.msa.msauserservice.config.properties"])
class MsaUserServiceApplication {
	@Bean
	fun runner(dataSource: DataSource): CommandLineRunner {
		return CommandLineRunner { _ -> println("URL: " + dataSource.connection.metaData.url) }
	}

	@Bean
	@LoadBalanced
	fun getRestTemplate(): RestTemplate {
		return RestTemplate()
	}

	@Bean
	fun feignLoggerLevel(): Logger.Level {
		return Logger.Level.FULL
	}
}

fun main(args: Array<String>) {
	DotenvLoader.load()

	runApplication<MsaUserServiceApplication>(*args)
}
