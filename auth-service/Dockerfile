FROM openjdk:21-jdk-slim
WORKDIR /app
COPY auth-service/target/auth-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
