package study.msa.msaorderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
class MsaOrderServiceApplication

fun main(args: Array<String>) {
    runApplication<MsaOrderServiceApplication>(*args)
}
