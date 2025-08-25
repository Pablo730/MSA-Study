# MSA-Study: Discovery Server (Eureka)

MSA Study 프로젝트의 모든 마이크로서비스 인스턴스를 등록하고 검색할 수 있도록 지원하는 **Spring Cloud Netflix Eureka 서버**입니다.

이 서버는 서비스 레지스트리(Service Registry)로서, 각 마이크로서비스가 시작될 때 자신의 네트워크 위치(IP, 포트)를 등록하고, 다른 서비스의 위치를 찾아갈 수 있도록 도와주는 "전화번호부" 역할을 수행합니다.

## ✨ 주요 기능 (Features)

* **서비스 등록 및 검색**: 모든 마이크로서비스가 자신의 위치를 동적으로 등록하고, 다른 서비스의 위치를 이름으로 조회할 수 있습니다.
* **서버 접근 제어**: Spring Security를 적용하여, 허가된 사용자(마이크로서비스)만 Eureka 서버에 접근하고 서비스를 등록할 수 있도록 HTTP Basic Authentication을 사용합니다.
* **환경 변수 관리**: `.env` 파일을 통해 로컬 환경의 민감한 정보(서버 포트, 보안 계정 등)를 코드와 분리하여 관리합니다.
* **컨테이너화**: Multi-stage Build를 활용한 `Dockerfile`을 통해, 가볍고 안전한 실행 전용 Docker 이미지를 생성합니다.

## 🛠️ 기술 스택 (Tech Stack)

* **Language**: Kotlin 1.9.25 (JVM 21)
* **Framework**: Spring Boot 3.5.4, Spring Cloud 2025.0.0
* **Build Tool**: Gradle
* **Containerization**: Docker

## ⚙️ 실행 전 준비사항 (Prerequisites)

서버를 실행하기 전에 환경 변수 파일을 반드시 준비해야 합니다.

### 환경 변수 파일 생성 (`.env`)

프로젝트 루트(`discovery-server/`)에 있는 `.env.sample` 파일을 복사하여 `.env` 파일을 생성합니다. 이 파일은 Git에 의해 추적되지 않으므로, 민감한 정보를 안전하게 보관할 수 있습니다.

```bash
# discovery-server 폴더 안에서 실행
cp .env.sample .env
```

그런 다음, `.env` 파일을 열어 자신의 로컬 환경에 맞게 값을 수정합니다.

```dotenv
# .env

# 서버 실행 포트
SERVER_PORT=8761

# Eureka 서버 접근 제어를 위한 사용자 정보
DISCOVERY_SERVER_USER=discovery_local
DISCOVERY_SERVER_PASSWORD={noop}discovery_local
```

> 💡 **Tip!** `{noop}` 접두사는 Spring Security에게 이 비밀번호가 암호화되지 않은 평문임을 알려주는 역할을 합니다.

## 🚀 실행 방법 (How to Run)

### 1. 로컬 환경에서 직접 실행

프로젝트를 빌드하여 JAR 파일을 생성한 뒤, 직접 실행합니다.

```bash
# 1. discovery-server 폴더로 이동
cd discovery-server

# 2. 프로젝트 빌드 (테스트 포함)
./gradlew clean build

# 3. 프로젝트 최상위 폴더(MSA-Study)로 이동
cd ..

# 4. JAR 파일 실행
java -jar discovery-server/build/libs/discovery-server-1.0.0.jar
```

### 2. Docker 컨테이너로 실행

`Dockerfile`을 사용하여 이미지를 빌드하고, 다른 MSA 서비스와 통신할 수 있도록 공용 네트워크에 연결하여 실행합니다.

```bash
# 1. 프로젝트 최상위 폴더(MSA-Study)에서 실행

# 2. 공용 네트워크 생성 (최초 한 번만)
docker network create msa-net

# 3. Docker 이미지 빌드
docker build -t discovery-server:1.0.0 ./discovery-server

# 4. Docker 컨테이너 실행
docker run --network msa-net --name discovery-server -p 8761:8761 discovery-server:1.0.0
```

## 🖥️ 대시보드 (Dashboard)

웹 브라우저를 통해 Eureka 서버의 대시보드에 접속하여 현재 등록된 서비스들의 상태를 실시간으로 확인할 수 있습니다.

* **URL**: `http://localhost:8761`
* **인증 정보**:
    * **Username**: `.env` 파일의 `DISCOVERY_SERVER_USER` 값 (예: `discovery_local`)
    * **Password**: `.env` 파일의 `DISCOVERY_SERVER_PASSWORD` 값 ( `{noop}` 제외, 예: `discovery_local`)
