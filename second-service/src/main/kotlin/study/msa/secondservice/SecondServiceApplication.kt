package study.msa.secondservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class SecondServiceApplication

fun main(args: Array<String>) {
    runApplication<SecondServiceApplication>(*args)
}
