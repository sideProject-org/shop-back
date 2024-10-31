# 빌드 스테이지
FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /app

# Gradle 래퍼와 설정 파일 복사 및 빌드
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
COPY . .
RUN ./gradlew clean build -x test

# 실행 스테이지 (최종 이미지)
FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
