FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY ./backend /app/.
RUN gradle wrapper
RUN ./gradlew backend-service:clean backend-service:build -x backend-service:test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/backend-service/build/libs/*SNAPSHOT.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]