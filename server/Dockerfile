FROM gradle:8.8-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY src /app/src

RUN gradle clean build -x test

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]