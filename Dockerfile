FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
RUN mkdir -p /app/uploads

ENV SAVING_DIR=/app/uploads

ENV GEOAPIFY_SECRET=86ad74aa384547249f62827e4a190907

COPY target/map-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]