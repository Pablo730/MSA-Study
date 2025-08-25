# MSA-Study: Gateway Server

MSA Study 프로젝트의 모든 마이크로서비스로 향하는 외부 요청을 처리하는 단일 진입점(Single Point of Entry) 역할을 하는 **Spring Cloud Gateway**입니다.

이 서버는 API 라우팅, 인증/인가, 로깅 등 모든 마이크로서비스에 공통적으로 적용되어야 할 횡단 관심사(Cross-cutting concerns)를 중앙에서 처리합니다. 또한, Eureka Discovery Server와 연동하여 동적으로 서비스의 위치를 찾아 라우팅하며, 모든 설정은 Config Server로부터 받아옵니다.

## ✨ 주요 기능 (Features)

* **동적 라우팅**: Eureka에 등록된 서비스 이름을 기반으로(`lb://SERVICE-NAME`), 각 마이크로서비스로 요청을 동적으로 라우팅합니다.
* **중앙화된 설정 관리**: 시작 시 Spring Cloud Config Server로부터 자신의 모든 설정(라우팅 규칙, 필터 등)을 받아옵니다.
* **서비스 디스커버리**: Eureka Discovery Server에 자신을 등록하고, 다른 서비스들의 네트워크 위치를 동적으로 찾아냅니다.
* **공통 필터 적용**: 모든 요청에 대한 글로벌 필터(로깅 등)와 각 라우팅 규칙에 맞는 커스텀 필터(인증 헤더 검증 등)를 적용합니다.
* **보안**: JWT(JSON Web Token) 기반의 인증 필터를 통해, 인증된 사용자의 요청만 내부 서비스로 전달하는 역할을 수행할 수 있습니다.
* **환경 변수 관리**: `.env` 파일을 통해 로컬 개발 환경의 설정을 코드와 분리하여 관리합니다.
* **컨테이너화**: Multi-stage Build를 활용한 `Dockerfile`을 통해, 가볍고 안전한 실행 전용 Docker 이미지를 생성합니다.

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Kotlin 1.9.25 (JVM 21)
* **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0 (Spring Cloud Gateway)
* **Build Tool**: Gradle
* **Service Discovery**: Netflix Eureka
* **Configuration**: Spring Cloud Config
* **Containerization**: Docker

## ⚙️ 실행 전 준비사항 (Prerequisites)

Gateway 서버를 실행하기 전에 아래 사항들을 반드시 준비해야 합니다.

### 1. 의존성 서비스 실행

이 Gateway 서버는 MSA의 핵심 인프라에 의존합니다. 따라서, **아래 서비스들이 반드시 먼저 실행되고 있어야 합니다.**

* **Eureka Discovery Server**: Config Server 및 다른 마이크로서비스들의 위치를 찾기 위해 필요합니다.
* **Config Server**: 자신의 모든 설정을 받아오기 위해 필요합니다.
* **RabbitMQ**: 설정 변경 전파 등 Spring Cloud Bus 기능을 위해 필요할 수 있습니다.

> 💡 위 서비스들은 프로젝트 루트의 `docker-compose.yml`을 통해 쉽게 실행할 수 있습니다.

### 2. 환경 변수 파일 생성 (`.env` / `.env.local`)

프로젝트 루트(`gateway-reactive/`)에 있는 `.env.sample` 파일을 복사하여 `.env` (Docker 실행용) 또는 `.env.local` (로컬 IDE 실행용) 파일을 생성하고, 자신의 환경에 맞게 값을 수정합니다.

```bash
# gateway-reactive 폴더 안에서 실행
$ cp .env.sample .env.local
$ cp .env.sample .env
```

**`.env.local` (로컬 IDE 실행용 예시)**

```dotenv
CONFIG_SERVICE_ID=config-service
CONFIG_SERVER_USER=config_local
CONFIG_SERVER_PASSWORD=config_local
CONFIG_NAME=gateway-service

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
CONFIG_NAME=gateway-service

SPRING_PROFILES_ACTIVE=default

EUREKA_USER=discovery_local
EUREKA_PASSWORD=discovery_local
EUREKA_URL=discovery-server:8761/eureka/
```

## 🚀 실행 방법 (How to Run)

> **중요**: 실행하기 전에 반드시 **Eureka Server와 Config Server가 먼저 실행 중**인지 확인하세요.

### 1. 로컬 환경에서 직접 실행

```bash
# 1. gateway-reactive 폴더에서 프로젝트 빌드
$ cd gateway-reactive
$ ./gradlew clean build

# 2. 프로젝트 최상위 폴더(MSA-Study)로 이동
$ cd ..

# 3. JAR 파일 실행
$ java -jar gateway-reactive/build/libs/gateway-reactive-1.0.0.jar
```

### 2. Docker 컨테이너로 실행

`docker-compose`를 사용하면 `depends_on` 옵션을 통해 서비스 시작 순서를 자동으로 관리할 수 있습니다.

```bash
# 1. 프로젝트 최상위 폴더(MSA-Study)에서 실행

# 2-1. docker-compose.yml 파일에 gateway-service가 포함되어 있는지 확인 후,
#    --build 옵션으로 이미지를 생성하며 모든 서비스를 함께 실행
$ docker-compose up --build -d

# 2-2. gateway-service만 별도로 실행 (이미지 빌드가 필요한 경우 --build 옵션 추가)
$ docker build -t gateway-service:1.0.0 ./gateway-reactive

$ docker run --network msa-net --name gateway-service -p 8000:8000 gateway-service:1.0.0
```

## ⚙️ 설정 관리 (Configuration Management)

Gateway의 모든 주요 설정(라우팅 규칙, 필터 등)은 **Config Server**를 통해 중앙에서 관리됩니다.

* **로컬 개발**: 로컬 IDE에서 실행 시, `gateway-reactive/src/main/resources/application.yml` 파일이 기본 설정으로 사용됩니다.
* **원격 환경 (Docker 등)**: 컨테이너로 실행 시, `bootstrap.yml`의 설정에 따라 Config Server의 Git Repository에 있는 `gateway-service.yml` 또는 `gateway-service-{profile}.yml` 파일을 읽어와 설정을 덮어씁니다.

설정 변경이 필요할 경우, Git Repository의 설정 파일을 수정한 뒤 Config Server의 `/busrefresh` 엔드포인트를 호출하면, Gateway 서버를 재배포하지 않고도 동적으로 설정을 갱신할 수 있습니다.
