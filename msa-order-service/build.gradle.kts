plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Jackson Kotlin module
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.11") // Kotlin logging

    implementation("org.springframework.boot:spring-boot-starter-web") // Spring Boot Web Starter
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Spring Boot Data JPA Starter
    implementation("org.springframework.boot:spring-boot-starter-actuator") // Spring Boot Actuator Starter

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") // Eureka Client

    implementation("org.springframework.kafka:spring-kafka") // Spring Kafka for messaging

    implementation("io.micrometer:micrometer-tracing-bridge-brave") // Micrometer Tracing with Brave
    implementation("io.zipkin.reporter2:zipkin-reporter-brave") // Zipkin Reporter for Brave
//    implementation("io.micrometer:micrometer-observation") // Micrometer Observation, Observability
    implementation("io.micrometer:micrometer-registry-prometheus")

    runtimeOnly("mysql:mysql-connector-java:8.0.33") // MySQL Connector for database access

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
