FROM amazoncorretto:21-alpine
WORKDIR /app
COPY config-server/target/config-server-*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
