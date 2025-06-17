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
VOLUME /app/uploads
ENV SAVING_DIR=/app/uploads
ENV GEOAPIFY_SECRET=${GEOAPIFY_SECRET}
ENV CLIENT_ID=86ad74aa384547249f62827e4a190907
ENV CLIENT_SECRET=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
ENV PORT=8080
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "app.jar"]