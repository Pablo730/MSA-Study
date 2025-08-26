package study.msa.msacatalogservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import study.msa.msacatalogservice.config.DotenvLoader

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = ["study.msa.msacatalogservice.config.properties"])
class MsaCatalogServiceApplication

fun main(args: Array<String>) {
	DotenvLoader.load()

	runApplication<MsaCatalogServiceApplication>(*args)
}
