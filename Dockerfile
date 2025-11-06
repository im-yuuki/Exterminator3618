# Build stage
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn -f ./server/pom.xml clean package spring-boot:repackage -DskipTests

# Image
FROM eclipse-temurin:21.0.8_9-jre-alpine
ARG version=1.3.6
LABEL authors="me@june8th.eu.org"
LABEL version="${version}"
WORKDIR /app
COPY --from=build /build/server/target/server-${version}.jar .
EXPOSE 36018/tcp
ENTRYPOINT ["java", "-jar", "server-1.3.6.jar"]
