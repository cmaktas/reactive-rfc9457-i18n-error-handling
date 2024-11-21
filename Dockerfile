FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src ./src
RUN chmod +x gradlew && ./gradlew build -x test
FROM openjdk:21 AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/errorhandler-0.0.1-SNAPSHOT.jar .
COPY src/main/resources/application.yml ./application.yml
ENTRYPOINT ["java", "-jar", "/app/errorhandler-0.0.1-SNAPSHOT.jar"]