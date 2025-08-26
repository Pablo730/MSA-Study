plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "study.msa"
version = "1.0.0"

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
	implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin Reflection, Required for Spring
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Jackson Kotlin Module, JSON Serialization/Deserialization
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.11") // Kotlin Logging, Logging for Kotlin
	implementation("io.github.cdimascio:dotenv-kotlin:6.5.1") // Dotenv library for environment variable management
	implementation("com.github.ben-manes.caffeine:caffeine") // Caffeine caching library

	implementation("org.springframework.boot:spring-boot-starter-web") // Web, REST API
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA, Hibernate
	implementation("org.springframework.boot:spring-boot-starter-actuator") // Actuator, Monitoring
	implementation("org.springframework.boot:spring-boot-starter-security") // Security, JWT
	implementation("org.springframework.boot:spring-boot-starter-cache") // Spring Boot Starter for caching

	implementation("io.jsonwebtoken:jjwt-api:0.12.6") // JJWT API, JSON Web Token
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6") // JJWT Implementation, JSON Web Token
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6") // JJWT Jackson, JSON Web Token with Jackson

	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") // Eureka Client, Service Discovery
	implementation("org.springframework.cloud:spring-cloud-starter-config") // Config Server, Spring Cloud Config
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp") // Spring Cloud Bus, RabbitMQ
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap") // Bootstrap, Spring Cloud Bootstrap
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign") // OpenFeign, Declarative REST Client
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j") // Resilience4j, Circuit Breaker

	implementation("io.micrometer:micrometer-tracing-bridge-brave") // Micrometer Tracing, Brave
	implementation("io.zipkin.reporter2:zipkin-reporter-brave") // Zipkin Reporter, Brave
	implementation("io.github.openfeign:feign-micrometer") // Feign Micrometer, Metrics for Feign Clients
	implementation("io.micrometer:micrometer-registry-prometheus") // Micrometer Prometheus Registry, Metrics Exporter

	runtimeOnly("mysql:mysql-connector-java:8.0.33") // MySQL Connector, Database Connectivity

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
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

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

springBoot {
	buildInfo()
}
