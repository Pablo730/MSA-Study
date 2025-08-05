package study.msa.discoveryclientsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class DiscoveryClientSampleApplication

fun main(args: Array<String>) {
	runApplication<DiscoveryClientSampleApplication>(*args)
}
