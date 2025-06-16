FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
RUN mkdir -p /app/uploads

# Копируем собранный JAR из этапа builder
COPY --from=builder /build/target/map-0.0.1-SNAPSHOT.jar app.jar

ENV SAVING_DIR=/app/uploads
ENV GEOAPIFY_SECRET=86ad74aa384547249f62827e4a190907

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]