# MSA-Study: Kubernetes 기반 마이크로서비스 아키텍처

본 프로젝트는 Spring Boot와 Kotlin을 사용하여 구축된 마이크로서비스 아키텍처(MSA) 학습 프로젝트입니다. 

초기에는 Spring Cloud 스택(Eureka, Config Server 등)을 활용하여 MSA의 기본 구성 요소를 직접 구축하고,
최종적으로는 모든 애플리케이션과 인프라를 **Kubernetes 클러스터** 위에서 운영하는 현대적인 클라우드 네이티브(Cloud-Native) 아키텍처로 전환하는 과정을 담고 있습니다.

이 문서는 프로젝트의 최종 아키텍처를 기준으로, 로컬 개발 환경에서 전체 시스템을 실행하는 방법을 안내합니다.
(초기 배포는 Docker Compose 기반 : https://github.com/Pablo730/MSA-Study/tree/deploy/docker-compose 참고)

---

## ✨ 최종 아키텍처

-   **애플리케이션 오케스트레이션**: Kubernetes (Docker Desktop 내장)
-   **인프라 (데이터 시스템)**: Kubernetes (Docker Desktop 내장)
-   **네트워크**: Pod 간 통신은 쿠버네티스 DNS를 통해 이루어지며, 외부 접근은 포트 포워딩을 사용

| 구분 | 구성 요소 | 실행 환경 | 역할 |
| :--- | :--- | :--- | :--- |
| **인프라** | `MySQL`, `Kafka (KRaft)`, `Kafka Connect`, `Kafka UI` | Kubernetes | 상태를 저장하는 핵심 데이터 시스템 |
| **애플리케이션** | `Gateway`, `User`, `Order`, `Catalog` | Kubernetes | 비즈니스 로직을 처리하는 무상태(Stateless) 서비스 |
| **모니터링** | `Prometheus`, `Grafana`, `Zipkin`, `Dashboard` | Kubernetes | 애플리케이션과 클러스터를 관찰하는 시스템 |

---

## 🛠️ 기술 스택

-   **Language**: Kotlin 1.9.25 (JVM 21)
-   **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
-   **Infrastructure**: Docker, Kubernetes (Docker Desktop)
-   **Database**: MySQL
-   **Messaging**: Apache Kafka (KRaft Mode)
-   **Observability**: Prometheus, Grafana, Zipkin

---

## 📁 디렉토리 구조
```
MSA-Study/
├── infra/                  # 📄 카프카 커넥터 이미지를 위한 도커 파일
│   └── Dockerfile-kafka-connect

├── k8s/                    # 🚀 애플리케이션 및 모니터링 시스템을 위한 Kubernetes 설정
│   ├── infra/              #   - MySQL, Kafka, Kafka Connect, Kafka UI 배포 파일
│   ├── monitoring/         #   - Prometheus, Grafana, Zipkin, Kubernetes Dashboard 배포 파일
│   ├── gateway/            #   - Gateway 배포 및 설정 파일
│   ├── user-service/       #   - User Service 배포 및 설정 파일
│   ├── catalog-service/    #   - Catalog Service 배포 및 설정 파일
│   └── order-service/      #   - Order Service 배포 및 설정 파일
│   └── start-dev.sh        #   - 모든 k8s 리소스 배포 및 포트 포워딩 실행 스크립트
│
├── gateway-reactive/       # Spring Cloud Gateway 서비스
├── msa-user-service/       # User 서비스
├── msa-catalog-service/    # Catalog 서비스
└── msa-order-service/      # Order 서비스
```

---

## 🚀 실행 방법 (로컬 개발 환경)

로컬 MacBook에서 전체 MSA 환경을 실행하는 과정입니다.

### 1. 사전 준비 (최초 한 번만)

#### Docker Desktop 설치 및 Kubernetes 활성화

URL: https://docs.docker.com/

세팅에서 Kubernetes 활성화 체크

#### 필수 Docker 이미지 빌드

프로젝트 최상위 디렉토리에서 다음 명령어를 실행하여 필수 도커 이미지를 빌드합니다.

```bash
$ docker build -t kafka-connect:1.0 ./infra

$ docker build -t gateway-service:1.0.0 ./gateway-reactive

$ docker build -t user-service:1.0.0 ./msa-user-service

$ docker build -t order-service:1.0.0 ./msa-order-service

$ docker build -t catalog-service:1.0.0 ./msa-catalog-service
```

### 2. 준비된 Kubernetes 설정 적용 및 포트 포워딩

프로젝트 최상위 디렉토리에서 다음 명령어를 실행하여 모든 Kubernetes 리소스를 배포하고, 필요한 포트 포워딩을 설정합니다.

```bash
# 1. k8s 폴더로 이동
$ cd k8s

# 2. 스크립트에 실행 권한 부여 (최초 한 번만)
$ chmod +x start-dev.sh

# 3. 스크립트 실행
$ ./start-dev.sh
```

#### 🔌 서비스 접속 정보

start-dev.sh 스크립트가 성공적으로 실행되면, 로컬 PC에서 아래 주소를 통해 각 서비스에 접속할 수 있습니다.

- API Gateway: http://localhost:8000/actuator/info
- User Service (직접 접속): http://localhost:8090/actuator/info
- Catalog Service (직접 접속): http://localhost:8095/actuator/info
- Order Service (직접 접속): http://localhost:8085/actuator/info
- MySQL (DB 툴): localhost:3306 (User: msa, PW: study)
- Kafka UI: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
- Zipkin: http://localhost:9411
- Kubernetes Dashboard: https://localhost:8443 (로그인 토큰 필요)
  - 토큰 발급 명령어: `kubectl -n kubernetes-dashboard create token admin-user`

## 주요 서비스 API 예시

로컬에서 k8s 설정을 통해 실행된 각 서비스의 주요 API 예시입니다.

### User Service

- Base URL: `http://localhost:8090`

아래 API는 Gateway를 통해서만 접근 가능합니다. (Gateway Base URL: `http://localhost:8000`)
디폴트 필터 조건으로 X-MSA-REQUEST 헤더가 포함되기 때문입니다. (gateway.secret)

#### 회원 생성
- URL: `POST http://localhost:8000/users`
- Body: 
```json
{
  "email": "test@test.com",
  "pwd": "12345678",
  "name": "test"
}
```
- 응답 예시:
```json
{
  "email": "test@test.com",
  "name": "test",
  "userId": "8b15802d-681a-49cf-b8e0-9d1acd20505b",
  "createdAt": "2025-08-29T12:55:18.265+00:00",
  "updatedAt": "2025-08-29T12:55:18.265+00:00"
}
```

#### 로그인
- URL: `POST http://localhost:8000/users/login`
- Body: 
```json
{
  "email" : "test@test.com",
  "password" : "12345678"
}
```
- 응답 예시: Response Header `token`에 JWT 토큰 포함

#### 회원 조회
로그인 시 Response Header의 `token` 값을 Authorization 헤더에 `Bearer {token}` 형식으로 포함하여 요청

- URL: `GET http://localhost:8000/users/{userId}`
- 응답 예시:
```json
{
  "userId": "8b15802d-681a-49cf-b8e0-9d1acd20505b",
  "orders": [],
  "email": "k@k.com",
  "name": "k",
  "createdAt": "2025-08-29T12:55:18.265+00:00",
  "updatedAt": "2025-08-29T12:55:18.265+00:00"
}
```

### Catalog Service
- Base URL: `http://localhost:8095`

#### 상품 조회
- URL: `GET http://localhost:8000/catalog-service/catalogs`
- 응답 예시:
```json
[
  {
    "productId": "CATALOG-001",
    "productName": "Berlin",
    "unitPrice": 1500,
    "stock": 100,
    "createdAt": "2025-08-13T00:00:00.000+00:00",
    "updatedAt": "2025-08-13T00:00:00.000+00:00"
  },
  {
    "productId": "CATALOG-002",
    "productName": "Tokyo",
    "unitPrice": 1000,
    "stock": 110,
    "createdAt": "2025-08-13T00:00:00.000+00:00",
    "updatedAt": "2025-08-13T00:00:00.000+00:00"
  }
]
```

### Order Service
- Base URL: `http://localhost:8085`

#### 주문 생성

올바른 주문 생성을 위해선 필히 카프카 싱크 커넥터가 정상적으로 동작 중이어야 합니다.
k8s 설정으로 배포된 카프카 커넥터로 싱크 커넥터를 등록하는 방법은 다음과 같습니다.

- URL: `POST http://localhost:8083/connectors`
- Body: 
```json
{
  "name" : "my-order-sink-connect",
  "config" : {
    "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url":"jdbc:mysql://mysql-service:3306/msa-study",
    "connection.user":"root",
    "connection.password":"root",
    "auto.create":"true",
    "auto.evolve":"true",
    "tasks.max":"1",
    "topics":"orders",
    "insert.mode": "insert",
    "pk.fields": "id"
  }
}
```

싱크 커넥터가 올바르게 등록되었다면 아래와 같이 주문을 생성할 수 있습니다.
- URL: `POST http://localhost:8000/order-service/{UserId}/orders`
- Body: 
```json
{
    "productId": "CATALOG-001",
    "qty": "10",
    "unitPrice": "10000"
}
```
- 응답 예시:
```json
{
  "productId": "CATALOG-001",
  "qty": 10,
  "unitPrice": 10000,
  "totalPrice": 100000,
  "orderId": "6e9c8192-e22c-408a-9d46-60abaee117f6",
  "createdAt": "2025-08-29T13:08:45.314+00:00"
}
```
