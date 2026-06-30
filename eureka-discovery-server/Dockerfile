FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml /workspace/pom.xml
COPY eureka-discovery-server/pom.xml /workspace/eureka-discovery-server/pom.xml
# Download dependencies
RUN mvn -B dependency:go-offline -pl eureka-discovery-server -am
# Copy source code and build
COPY eureka-discovery-server/src /workspace/eureka-discovery-server/src
RUN mvn -B clean package -pl eureka-discovery-server -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/eureka-discovery-server/target/eureka-discovery-server-*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
