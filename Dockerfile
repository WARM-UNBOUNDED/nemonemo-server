# 빌드 스테이지
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test && \
    mv build/libs/*.jar app.jar

# 실행 스테이지
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
