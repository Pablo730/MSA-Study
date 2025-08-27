package study.msa.msaorderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import study.msa.msaorderservice.config.DotenvLoader

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = ["study.msa.msaorderservice.config.properties"])
class MsaOrderServiceApplication

fun main(args: Array<String>) {
    DotenvLoader.load()

    runApplication<MsaOrderServiceApplication>(*args)
}
