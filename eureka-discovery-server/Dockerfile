FROM amazoncorretto:21-alpine
WORKDIR /app
COPY eureka-discovery-server/target/eureka-discovery-server-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
