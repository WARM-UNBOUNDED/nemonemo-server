FROM gradle:7.6-jdk17 AS build

WORKDIR /app

COPY . .

RUN rm -rf ~/.gradle/caches

RUN gradle build --no-daemon -x test

RUN mv build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8081