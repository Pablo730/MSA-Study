package study.msa.myfirstservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class MyFirstServiceApplication

fun main(args: Array<String>) {
    runApplication<MyFirstServiceApplication>(*args)
}
