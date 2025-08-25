# MSA-Study: Config Server

MSA Study 프로젝트의 모든 마이크로서비스 설정을 중앙에서 관리하는 **Spring Cloud Config Server**입니다.

이 서버는 Git Repository에 저장된 설정 파일들을 읽어 각 서비스에 제공하며, Spring Cloud Bus와 RabbitMQ를 통해 재배포 없이 동적으로 설정을 변경하고 전파하는 기능을 지원합니다. 또한, 민감 정보는 암호화하여 안전하게 관리합니다.

---

## ✨ 주요 기능 (Features)

* **중앙화된 설정 관리**: Git을 사용하여 모든 마이크로서비스의 설정을 한 곳에서 버전 관리합니다.
* **동적 설정 갱신**: Spring Cloud Bus와 RabbitMQ를 연동하여, Git 설정 변경 시 `/busrefresh` API 호출 한 번으로 모든 서비스 인스턴스에 설정을 실시간으로 전파합니다.
* **민감 정보 암호화**: JKS(Java KeyStore)를 사용한 대칭키 암호화 방식으로, 데이터베이스 비밀번호 등 민감 정보를 안전하게 암호화하여 Git에 저장합니다.
* **서버 접근 제어**: Spring Security를 적용하여, 허가된 사용자(마이크로서비스)만 설정 정보에 접근할 수 있도록 HTTP Basic Authentication을 사용합니다.
* **타입-세이프 설정 및 유효성 검증**: `@ConfigurationProperties`를 활용하여 `yml` 설정 값들을 타입이 안전한 Kotlin 데이터 클래스로 매핑하고, 애플리케이션 시작 시점에 유효성을 검증합니다.
* **환경 변수 관리**: `.env` 파일을 통해 로컬 환경의 민감 정보를 코드와 분리하여 관리합니다.
* **컨테이너화**: Multi-stage Build를 활용한 `Dockerfile`을 통해, 가볍고 안전한 실행 전용 Docker 이미지를 생성합니다.

---

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Kotlin 1.9.25 (JVM 21)
* **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
* **Build Tool**: Gradle
* **Messaging**: RabbitMQ
* **Containerization**: Docker

---

## ⚙️ 실행 전 준비사항 (Prerequisites)

서버를 실행하기 전에 아래 두 가지를 반드시 준비해야 합니다.

### 1. 암호화 키 생성 (`apiEncryptKey.jks`)

민감 정보를 암호화/복호화하는 데 사용할 JKS(Java KeyStore) 파일을 생성합니다. `config-service` 폴더에서 아래 `keytool` 명령어를 실행하세요.

> 💡 **Tip!** `keytool`은 JDK에 포함되어 있으므로, Java가 설치되어 있다면 바로 사용할 수 있습니다.

```bash
$ keytool -genseckey -alias apiEncryptKey -storetype JKS -keyalg AES -keysize 256 -keystore apiEncryptKey.jks
```
명령어를 실행하면 keystore 비밀번호를 두 번 입력하라고 나옵니다. 이 비밀번호는 아래 .env 파일의 KEY_STORE_PASSWORD 값과 반드시 일치해야 합니다.

### 2. 환경 변수 파일 생성 (.env)
프로젝트 루트(config-service/)에 있는 .env.sample 파일을 복사하여 .env 파일을 생성합니다. 이 파일은 Git에 의해 추적되지 않으므로, 민감한 정보를 안전하게 보관할 수 있습니다.

# config-service 폴더 안에서 실행
```bash
$ cp .env.sample .env
```
그런 다음, .env 파일을 열어 자신의 로컬 환경에 맞게 값을 수정합니다.

# 🚀 실행 방법 (How to Run)
### 1. 로컬 환경에서 직접 실행
프로젝트를 빌드하여 JAR 파일을 생성한 뒤, 직접 실행합니다.
```bash
# 1. config-service 폴더로 이동
$ cd config-service

# 2. 테스트를 포함하여 프로젝트 빌드
$ ./gradlew clean build

# 3. 프로젝트 최상위 폴더(MSA-Study)로 이동
$ cd ..

# 4. JAR 파일 실행
$ java -jar config-service/build/libs/config-service-1.0.0.jar
```

### 2. Docker 컨테이너로 실행
Dockerfile을 사용하여 이미지를 빌드하고, docker-compose로 인프라와 함께 실행합니다.
```bash
# 1. 프로젝트 최상위 폴더(MSA-Study)에서 실행

# 2. Docker 이미지 빌드
$ docker build -t config-service:1.0 ./config-service

# 3-1. docker-compose.yml 파일에 config-service가 포함되어 있는지 확인 후,
#    모든 서비스를 함께 실행 (local 환경 기준)
$ docker-compose up -d

# 3-2. docker-compose.yml 파일에 config-service가 포함되어 있지 않다면,
#    docker run 명령어로 단독 실행 (local 환경 기준)
$ docker run -d -p 8888:8888 --network msa-net \
    -e "spring.rabbitmq.host=rabbitmq" \
    --name config-service config-service:1.0
```

# 🔌 API 엔드포인트
Postman과 같은 API 클라이언트를 사용하여 아래 엔드포인트를 테스트할 수 있습니다. 모든 요청에는 Basic Auth 인증이 필요합니다. (Username: config_local, Password: config_local)

### 1. 설정 정보 조회
Git Repository에 저장된 설정 파일을 조회합니다.

URL: http://localhost:8888/{application-name}/{profile}

Method: GET

```bash
# 예시
$ curl -u "config_local:config_local" http://localhost:8888/ecommerce/dev
```

### 2. 민감 정보 암호화
Git에 저장할 민감 정보를 암호화된 문자열로 변환합니다.

URL: http://localhost:8888/encrypt

Method: POST

Headers: Content-Type: text/plain

Body: 암호화할 평문 (예: my_secret_db_password)

```bash
#예시
$ curl -u "config_local:config_local" -X POST \
  -H "Content-Type: text/plain" \
  --data "my_secret_db_password" \
  http://localhost:8888/encrypt
# 응답: {cipher}AQA... 와 같은 암호화된 문자열
```

### 3. 설정 변경 전파
Git 설정 변경 후, 모든 마이크로서비스에 변경 사항을 전파합니다.

URL: http://localhost:8888/actuator/busrefresh

Method: POST

```bash
# 예시
$ curl -u "config_local:config_local" -X POST http://localhost:8888/actuator/busrefresh
```
