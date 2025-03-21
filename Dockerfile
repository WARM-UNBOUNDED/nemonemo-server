FROM gradle:7.6-jdk17 AS build

WORKDIR /app
COPY . .

# Gradle 캐시 삭제
RUN rm -rf ~/.gradle/caches

# Gradle 빌드
RUN gradle build --no-daemon -x test

RUN mv build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

RUN gradle build --no-daemon -x test --stacktrace