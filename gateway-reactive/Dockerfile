# --- 1단계: 빌드 환경 ---
# JDK가 포함된 이미지에서 Gradle을 사용하여 프로젝트를 빌드합니다.
FROM openjdk:21-jdk-slim-bookworm AS builder

# 작업 디렉토리 설정
WORKDIR /workspace

# 먼저 의존성 관련 파일만 복사하여 캐싱을 활용합니다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN ./gradlew dependencies --no-daemon

# 전체 소스 코드를 복사합니다.
COPY . .

# 테스트를 제외하고 빌드하여 실행 가능한 JAR 파일을 생성합니다.
RUN ./gradlew build -x test --no-daemon


# --- 2단계: 최종 실행 환경 ---
# JRE만 포함된 더 가벼운 이미지로 전환합니다.
FROM openjdk:21-slim-bookworm

# 애플리케이션이 실행될 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 실행 가능한 JAR 파일만 최종 이미지로 복사합니다.
COPY --from=builder /workspace/build/libs/*.jar ./app.jar

# Gateway가 사용할 포트를 외부에 노출
EXPOSE 8000

# 컨테이너가 시작될 때 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
