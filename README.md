# MSA-Study: Docker Compose 기반 마이크로서비스 아키텍처

본 프로젝트는 Spring Boot와 Kotlin을 사용하여 구축된 마이크로서비스 아키텍처(MSA) 학습 프로젝트입니다. 이 브랜치(`deploy/docker-compose`)는 **Spring Cloud 스택(Eureka, Config Server, Spring Cloud Bus 등)을 활용**하여 MSA의 모든 구성 요소를 구축하고, **Docker Compose를 통해 로컬 환경에 전체 시스템을 배포**하는 과정을 담고 있습니다.

이 문서는 프로젝트의 아키텍처를 설명하고, 로컬 개발 환경에서 전체 시스템을 실행하는 방법을 안내합니다.

> 💡 **참고**: 최종적으로 Kubernetes로 전환된 버전은 `main` 브랜치를 참고하세요.

---

## ✨ 아키텍처

- **서비스 오케스트레이션**: Docker Compose
- **서비스 디스커버리**: Spring Cloud Netflix Eureka
- **중앙 설정 관리**: Spring Cloud Config Server (with Git Backend)
- **동적 설정 갱신**: Spring Cloud Bus (with RabbitMQ)
- **API 게이트웨이**: Spring Cloud Gateway
- **비동기 통신**: Apache Kafka & Kafka Connect
- **분산 추적**: Zipkin
- **메트릭 수집/시각화**: Prometheus & Grafana

---

## 🛠️ 기술 스택

- **Language**: Kotlin 1.9.25 (JVM 21)
- **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
- **Infrastructure**: Docker, Docker Compose
- **Database**: MySQL
- **Messaging**: RabbitMQ, Apache Kafka (with Zookeeper)
- **Observability**: Prometheus, Grafana, Zipkin

---

## 📁 디렉토리 구조
```
MSA-Study/
├── infra/                  # 📄 핵심 인프라(DB, MQ, 모니터링 등)를 위한 Docker Compose 설정
│   ├── docker-compose.yml
│   └── Dockerfile          # (Kafka Connect용)
│
├── docker-compose.yml      # 🚀 애플리케이션 서비스들을 위한 Docker Compose 설정
│
├── config-service/         # Spring Cloud Config Server
├── discovery-server/       # Spring Cloud Eureka Server
├── gateway-reactive/       # Spring Cloud Gateway 서비스
├── msa-user-service/       # User 서비스
├── msa-catalog-service/    # Catalog 서비스
└── msa-order-service/      # Order 서비스
```
## 🚀 실행 방법 (로컬 개발 환경)

로컬 PC에서 전체 MSA 환경을 실행하는 과정입니다.

### 1. 사전 준비 (최초 한 번만)

#### 1-1. 공용 네트워크 생성
모든 컨테이너가 서로 통신할 수 있는 Docker 네트워크를 생성합니다.
```bash
$ docker network create msa-net
```

#### 1-2. 환경 변수 파일 생성 (.env & .env.local)
각 서비스 폴더(config-service, discovery-server 등)에는 로컬 IDE 실행을 위한 .env.local 파일이 포함되어 있습니다. Docker Compose로 실행하기 위해 이 파일들을 복사하여 .env 파일을 생성해야 합니다.

💡 Tip! 아래 스크립트를 프로젝트 최상위 폴더에서 실행하면 모든 .env 파일을 한 번에 생성할 수 있습니다.

```bash
$ find . -name ".env.local" -exec sh -c 'cp "$1" "${1%.local}"' _ {} \;
1-3. Config Server 암호화 키 생성
config-service가 민감 정보를 암호화하는 데 사용할 JKS 키스토어 파일을 생성합니다.
# config-service 폴더로 이동
$ cd config-service

# keytool 명령어로 키 생성 (JDK 필요)
$ keytool -genseckey -alias apiEncryptKey -storetype JKS -keyalg AES -keysize 256 -keystore apiEncryptKey.jks
```
⚠️ 명령어 실행 시 설정하는 비밀번호는 config-service/.env 파일의 KEY_STORE_PASSWORD 값과 반드시 일치해야 합니다.


2. 인프라 서비스 실행
infra 폴더로 이동하여 MySQL, Kafka, RabbitMQ 등 핵심 인프라를 먼저 실행합니다.

```bash
# 1. infra 폴더로 이동
$ cd infra

# 2. 인프라 서비스들을 백그라운드에서 실행
$ docker-compose up -d
```

### 3. 애플리케이션 서비스 실행
인프라가 모두 준비되면, 프로젝트 최상위 폴더에서 애플리케이션 서비스를 실행합니다. --build 옵션을 사용하여 각 서비스의 Docker 이미지를 생성하며 실행합니다.

```bash
# 1. 프로젝트 최상위 폴더로 이동
$ cd ..

# 2. 이미지를 빌드하면서 애플리케이션들을 백그라운드에서 실행
$ docker-compose up --build -d
```

### 🔌 서비스 접속 정보
docker-compose가 성공적으로 실행되면, 아래 주소를 통해 각 서비스에 접속할 수 있습니다.

- API Gateway: http://localhost:8000
- Eureka Dashboard: http://localhost:8761 (ID: discovery_local, PW: discovery_local)
- Config Server: http://localhost:8888 (ID: config_local, PW: config_local)
- Kafka UI: http://localhost:8080
- RabbitMQ Management: http://localhost:15672 (ID: admin, PW: admin)
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
- Zipkin: http://localhost:9411
- MySQL (DB 툴): localhost:3306 (User: msa, PW: study)
