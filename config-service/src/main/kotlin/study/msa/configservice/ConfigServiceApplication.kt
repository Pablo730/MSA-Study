package study.msa.configservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import study.msa.configservice.config.DotenvLoader

@SpringBootApplication
@EnableConfigServer
@ConfigurationPropertiesScan(basePackages = ["study.msa.configservice.config.properties"])
class ConfigServiceApplication

fun main(args: Array<String>) {
	DotenvLoader.load() // .env 파일 로드

	runApplication<ConfigServiceApplication>(*args)
}
