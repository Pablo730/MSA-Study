plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
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
	implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin reflection library
	implementation("io.github.cdimascio:dotenv-kotlin:6.5.1") // Dotenv library for environment variable management

	implementation("org.springframework.boot:spring-boot-starter-validation") // Spring Boot Starter for validation
	implementation("org.springframework.boot:spring-boot-starter-actuator") // Spring Boot Actuator for monitoring and management

	implementation("org.springframework.cloud:spring-cloud-config-server") // Spring Cloud Config Server for centralized configuration management
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp") // Spring Cloud Bus with AMQP for distributed messaging

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

tasks.withType<Test> {
	useJUnitPlatform()
}

springBoot {
	buildInfo()
}
