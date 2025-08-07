package study.msa.msauserservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource


@SpringBootApplication
@EnableDiscoveryClient
class MsaUserServiceApplication {
	@Bean
	fun runner(dataSource: DataSource): CommandLineRunner {
		return CommandLineRunner { _ -> println("URL: " + dataSource.connection.metaData.url) }
	}
}

fun main(args: Array<String>) {
	runApplication<MsaUserServiceApplication>(*args)
}
