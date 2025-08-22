plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "study.msa"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin reflection
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions") // Reactor Kotlin Extensions
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor") // Kotlin Coroutines for Reactor
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.11") // Kotlin Logging

	implementation("org.springframework.boot:spring-boot-starter-actuator") // Spring Boot Actuator

	implementation("io.jsonwebtoken:jjwt-api:0.12.6") // JJWT API for JWT handling
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6") // JJWT Implementation for JWT handling
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6") // JJWT Jackson for JSON processing

	implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux") // Spring Cloud Gateway with WebFlux
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") // Eureka Client for Service Discovery
	implementation("org.springframework.cloud:spring-cloud-starter-config") // Spring Cloud Config Client
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp") // Spring Cloud Bus with AMQP
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap") // Spring Cloud Bootstrap for Configuration

	implementation("io.micrometer:micrometer-tracing-bridge-brave") // Micrometer Tracing with Brave
	implementation("io.zipkin.reporter2:zipkin-reporter-brave") // Zipkin Reporter for Brave
	implementation("io.micrometer:micrometer-registry-prometheus") // Micrometer Prometheus Registry

	runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.94.Final:osx-aarch_64") // Netty DNS Resolver for macOS AArch64

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
