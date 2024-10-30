# Java 17 이미지로 변경
FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

# Gradle 빌드를 수행하여 JAR 파일을 생성
COPY . .
RUN ./gradlew clean build

# 생성된 JAR 파일을 이미지에 포함
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# 환경 변수를 로드하고 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]