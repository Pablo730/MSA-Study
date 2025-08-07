package study.msa.msaorderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class MsaOrderServiceApplication

fun main(args: Array<String>) {
    runApplication<MsaOrderServiceApplication>(*args)
}
