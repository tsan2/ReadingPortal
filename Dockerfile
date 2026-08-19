FROM eclipse-temurin:25-jdk-alpine-3.23 AS build_stage
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests
FROM eclipse-temurin:25-jre-alpine-3.23
WORKDIR /app
COPY --from=build_stage /app/target/readingportal-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]