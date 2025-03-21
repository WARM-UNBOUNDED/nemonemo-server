# 1. Gradle 기반으로 빌드 환경 설정
FROM gradle:7.6-jdk17 AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 애플리케이션 소스 코드 복사
COPY . .

# 4. Gradle 캐시 삭제 (빌드 속도 향상)
RUN rm -rf ~/.gradle/caches

# 5. Gradle 빌드 (테스트 제외)
RUN gradle build --no-daemon -x test

# 6. 빌드된 JAR 파일을 app.jar로 이동
# 'sns-server-0.0.1-SNAPSHOT.jar'로 설정하여 JAR 파일을 app.jar로 이동
RUN mv build/libs/sns-server-0.0.1-SNAPSHOT.jar app.jar

# 7. 애플리케이션 실행을 위한 ENTRYPOINT 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 8. 애플리케이션이 사용할 포트 열기
EXPOSE 8081
