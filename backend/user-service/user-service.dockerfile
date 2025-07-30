FROM gradle:8.7-jdk17 AS builder
WORKDIR /app
COPY ./backend /app/.
RUN gradle wrapper
RUN ./gradlew user-service:clean user-service:build -x user-service:test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/user-service/build/libs/*SNAPSHOT.jar /app/*.jar

COPY ./ca-certs/certs/ /usr/local/share/ca-certificates
RUN for cert in /usr/local/share/ca-certificates/*.crt; do cert2=$(basename $cert); keytool -importcert -noprompt -trustcacerts -alias ${cert2} -file ${cert} -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit; done

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]