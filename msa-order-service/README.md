# MSA-Study: Order Service

MSA Study 프로젝트의 주문 관련 비즈니스 로직을 처리하는 **주문 마이크로서비스**입니다.

이 서비스는 주문 생성, 조회 등의 핵심 기능을 담당합니다. 특히, 주문 데이터의 최종적인 데이터베이스 저장을 동기 방식이 아닌, Apache Kafka와 Kafka Connect를 사용하는 **이벤트 기반 아키텍처**로 구현한 것이 특징입니다.

---

## ✨ 주요 기능 (Features)

* **주문 관리**: 주문 생성 및 조회 등 주문 관련 API를 제공합니다.
* **이벤트 기반 데이터 영속성**: 주문 생성 시, 직접 DB에 저장하는 대신 Kafka 토픽(`orders`)으로 주문 이벤트를 발행합니다. 실제 DB 저장은 별도의 Kafka Connect Sink Connector가 담당합니다.
* **중앙화된 설정 관리**: 시작 시 Spring Cloud Config Server로부터 자신의 모든 설정을 받아옵니다.
* **서비스 디스커버리**: Eureka Discovery Server에 자신을 등록하고, 다른 서비스들의 네트워크 위치를 동적으로 찾아냅니다.
* **동적 설정 갱신**: Spring Cloud Bus를 통해, 재배포 없이 동적으로 설정을 갱신할 수 있습니다.
* **컨테이너화**: Multi-stage Build를 활용한 `Dockerfile`을 통해, 가볍고 안전한 실행 전용 Docker 이미지를 생성합니다.

---

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Kotlin 1.9.25 (JVM 21)
* **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
* **Database**: MySQL, Spring Data JPA
* **Build Tool**: Gradle
* **Service Discovery**: Netflix Eureka
* **Configuration**: Spring Cloud Config
* **Messaging**: RabbitMQ (for Spring Cloud Bus), **Apache Kafka**, **Kafka Connect**
* **Containerization**: Docker

---

## ⚙️ 실행 전 준비사항 (Prerequisites)

Order Service를 실행하기 전에 아래 사항들을 반드시 준비해야 합니다.

### 1. 의존성 서비스 실행

이 서비스는 MSA의 핵심 인프라에 의존합니다. 따라서, **아래 서비스들이 반드시 먼저 실행되고 있어야 합니다.**

* **Eureka Discovery Server**
* **Config Server**
* **RabbitMQ**
* **MySQL**
* **Kafka** & **Zookeeper**
* **Kafka Connect**

> 💡 위 서비스들은 프로젝트 루트의 `infra/docker-compose.yml`을 통해 쉽게 실행할 수 있습니다.

### 2. Kafka Connect Sink Connector 등록 (매우 중요)

이 서비스는 주문 정보를 직접 DB에 저장하지 않습니다. 대신 `orders` 토픽으로 메시지를 발행하면, Kafka Connect의 **JDBC Sink Connector**가 이 메시지를 구독하여 DB에 저장합니다. 따라서, **아래 Sink Connector를 반드시 Kafka Connect에 등록해야만 주문 정보가 DB에 정상적으로 저장됩니다.**

Postman 등을 사용하여 Kafka Connect REST API(`http://localhost:8083`)로 아래 요청을 보내세요.

* **URL**: `http://localhost:8083/connectors/`
* **Method**: `POST`
* **Body**:
    ```json
    {
        "name": "my-order-sink-connect",
        "config": {
            "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
            "connection.url": "jdbc:mysql://mysql:3306/msa-order-1",
            "connection.user": "root",
            "connection.password": "root",
            "auto.create": "true",
            "auto.evolve": "true",
            "tasks.max": "1",
            "topics": "orders",
            "insert.mode": "insert",
            "pk.mode": "record_key",
            "pk.fields": "id"
        }
    }
    ```

### 3. 환경 변수 파일 생성 (`.env` / `.env.local`)

프로젝트 루트(`msa-order-service/`)에 있는 `.env.sample` 파일을 복사하여 `.env` (Docker 실행용) 또는 `.env.local` (로컬 IDE 실행용) 파일을 생성합니다.

```bash
# msa-order-service 폴더 안에서 실행
$ cp .env.sample .env.local
$ cp .env.sample .env
```

---

## 🚀 실행 방법 (How to Run)

> **중요**: 실행하기 전에 반드시 모든 의존성 인프라 서비스가 실행 중이고, Kafka Connect Sink Connector가 등록되었는지 확인하세요.

### Docker 컨테이너로 실행
`docker-compose`를 사용하면 `depends_on` 옵션을 통해 서비스 시작 순서를 자동으로 관리할 수 있습니다.

```bash
# 1. 프로젝트 최상위 폴더(MSA-Study)에서 실행
# 2. docker-compose.yml 파일에 order-service가 포함되어 있는지 확인 후,
#    --build 옵션으로 이미지를 생성하며 모든 서비스를 함께 실행
$ docker-compose up --build -d
```
