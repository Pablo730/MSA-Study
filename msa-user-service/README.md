# MSA-Study: User Service

MSA Study 프로젝트의 사용자 관련 비즈니스 로직을 처리하는 **사용자 마이크로서비스**입니다.

이 서비스는 회원 가입, 로그인, 사용자 정보 조회 등의 핵심 기능을 담당하며, Spring Data JPA를 통해 MySQL 데이터베이스와 통신합니다. 또한, 다른 마이크로서비스와의 통신을 위해 OpenFeign 클라이언트를 사용합니다.

## ✨ 주요 기능 (Features)

* **사용자 관리**: 회원 가입, 로그인, 사용자 정보 조회 등 사용자 관련 CRUD 기능을 제공합니다.
* **데이터 영속성**: Spring Data JPA와 Hibernate를 사용하여 사용자 정보를 MySQL 데이터베이스에 저장하고 관리합니다.
* **중앙화된 설정 관리**: 시작 시 Spring Cloud Config Server로부터 자신의 모든 설정을 받아옵니다.
* **서비스 디스커버리**: Eureka Discovery Server에 자신을 등록하고, 다른 서비스들의 네트워크 위치를 동적으로 찾아냅니다.
* **동적 설정 갱신**: Spring Cloud Bus를 통해, 재배포 없이 동적으로 설정을 갱신할 수 있습니다.
* **보안**: Spring Security를 적용하여 비밀번호를 암호화하고, API 접근 제어를 처리합니다.
* **타입-세이프 설정 및 유효성 검증**: `@ConfigurationProperties`를 활용하여 `yml` 설정 값들을 타입이 안전한 Kotlin 데이터 클래스로 매핑하고 유효성을 검증합니다.
* **컨테이너화**: Multi-stage Build를 활용한 `Dockerfile`을 통해, 가볍고 안전한 실행 전용 Docker 이미지를 생성합니다.

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Kotlin 1.9.25 (JVM 21)
* **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
* **Database**: MySQL, Spring Data JPA
* **Build Tool**: Gradle
* **Service Discovery**: Netflix Eureka
* **Configuration**: Spring Cloud Config
* **Messaging**: RabbitMQ (for Spring Cloud Bus)
* **Containerization**: Docker

## ⚙️ 실행 전 준비사항 (Prerequisites)

User Service를 실행하기 전에 아래 사항들을 반드시 준비해야 합니다.

### 1. 의존성 서비스 실행

이 서비스는 MSA의 핵심 인프라에 의존합니다. 따라서, **아래 서비스들이 반드시 먼저 실행되고 있어야 합니다.**

* **Eureka Discovery Server**
* **Config Server**
* **RabbitMQ**
* **MySQL**

> 💡 위 서비스들은 프로젝트 루트의 `infra/docker-compose.yml`을 통해 쉽게 실행할 수 있습니다.

### 2. 환경 변수 파일 생성 (`.env` / `.env.local`)

프로젝트 루트(`msa-user-service/`)에 있는 `.env.sample` 파일을 복사하여 `.env` (Docker 실행용) 또는 `.env.local` (로컬 IDE 실행용) 파일을 생성하고, 자신의 환경에 맞게 값을 수정합니다.

```bash
# msa-user-service 폴더 안에서 실행
cp .env.sample .env.local
cp .env.sample .env
```

**`.env.local` (로컬 IDE 실행용 예시)**
```dotenv
CONFIG_SERVICE_ID=config-service
CONFIG_SERVER_USER=config_local
CONFIG_SERVER_PASSWORD=config_local
CONFIG_NAME=msa-user-service

SPRING_PROFILES_ACTIVE=local

EUREKA_USER=discovery_local
EUREKA_PASSWORD=discovery_local
EUREKA_URL=localhost:8761/eureka/
```

**`.env` (Docker 컨테이너 실행용 예시)**
```dotenv
CONFIG_SERVICE_ID=config-service
CONFIG_SERVER_USER=config_local
CONFIG_SERVER_PASSWORD=config_local
CONFIG_NAME=msa-user-service

SPRING_PROFILES_ACTIVE=default

EUREKA_USER=discovery_local
EUREKA_PASSWORD=discovery_local
EUREKA_URL=discovery-server:8761/eureka/
```

## 🚀 실행 방법 (How to Run)

> **중요**: 실행하기 전에 반드시 **Eureka, Config Server, RabbitMQ, MySQL이 먼저 실행 중**인지 확인하세요.

### 1. 로컬 환경에서 직접 실행
```bash
# 1. msa-user-service 폴더에서 프로젝트 빌드
cd msa-user-service
./gradlew clean build

# 2. 프로젝트 최상위 폴더(MSA-Study)로 이동
cd ..

# 3. JAR 파일 실행
java -jar msa-user-service/build/libs/msa-user-service-1.0.0.jar
```

### 2. Docker 컨테이너로 실행
`docker-compose`를 사용하면 `depends_on` 옵션을 통해 서비스 시작 순서를 자동으로 관리할 수 있습니다.

```bash
# 1. 프로젝트 최상위 폴더(MSA-Study)에서 실행

# 2. docker-compose.yml 파일에 user-service가 포함되어 있는지 확인 후,
#    --build 옵션으로 이미지를 생성하며 모든 서비스를 함께 실행
docker-compose up --build -d
```

## ⚙️ 설정 관리 (Configuration Management)

User Service의 모든 주요 설정(데이터베이스 정보, 토큰 비밀키 등)은 **Config Server**를 통해 중앙에서 관리됩니다.

* **로컬 개발**: 로컬 IDE에서 실행 시, `msa-user-service/src/main/resources/application.yml` 파일이 기본 설정으로 사용됩니다.
* **원격 환경 (Docker 등)**: 컨테이너로 실행 시, `bootstrap.yml`의 설정에 따라 Config Server의 Git Repository에 있는 `msa-user-service.yml` 또는 `msa-user-service-{profile}.yml` 파일을 읽어와 설정을 덮어씁니다.

설정 변경이 필요할 경우, Git Repository의 설정 파일을 수정한 뒤 Config Server의 `/busrefresh` 엔드포인트를 호출하면, User Service를 재배포하지 않고도 동적으로 설정을 갱신할 수 있습니다.
