FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml /workspace/pom.xml
COPY config-server/pom.xml /workspace/config-server/pom.xml
# Download dependencies
RUN mvn -B dependency:go-offline -pl config-server -am
# Copy source code and build
COPY config-server/src /workspace/config-server/src
RUN mvn -B clean package -pl config-server -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/config-server/target/config-server-*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
