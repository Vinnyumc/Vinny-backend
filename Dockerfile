# 1단계: 빌드용 이미지
FROM eclipse-temurin:17-jdk as build
WORKDIR /app

# gradlew 및 설정 파일 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# gradlew 실행 권한 추가
RUN chmod +x ./gradlew

# 의존성 캐싱
RUN ./gradlew dependencies

# 전체 소스 복사
COPY . .

# gradlew 실행 권한 재추가
RUN chmod +x ./gradlew

# 빌드 실행
RUN ./gradlew bootJar

# 2단계: 실행용 이미지 (경량화)
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 단계에서 생성된 JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 8080 포트 오픈
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java","-jar","app.jar"]

