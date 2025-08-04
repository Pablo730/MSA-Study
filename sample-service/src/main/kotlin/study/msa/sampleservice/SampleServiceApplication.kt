package study.msa.sampleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class SampleServiceApplication

fun main(args: Array<String>) {
	runApplication<SampleServiceApplication>(*args)
}
