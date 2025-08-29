package study.msa.msacatalogservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MsaCatalogServiceApplication

fun main(args: Array<String>) {
	runApplication<MsaCatalogServiceApplication>(*args)
}
