FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /build/target/map-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/uploads
ENV SAVING_DIR=/app/uploads
ENV GEOAPIFY_SECRET=${GEOAPIFY_SECRET}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]