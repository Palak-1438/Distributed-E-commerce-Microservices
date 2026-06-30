# Config Server

This module acts as the centralized configuration server for the Distributed E-commerce Microservices platform. It loads configuration files from the local file system (or a Git repository) and serves them to all other microservices at startup.

## 🚀 Technologies Used
- Java 21
- Spring Boot 3
- Spring Cloud Config Server

## ⚙️ Configuration
The server runs on port `8888`. It uses the `native` profile to read YAML files from the `config-repo` directory located in the project root.
Other microservices will fetch their configuration by connecting to `http://localhost:8888`.

## 🛠️ How to Run
From the root directory:
```bash
cd config-server
mvn spring-boot:run
```

You can verify the configuration is being served by visiting:
`http://localhost:8888/application/default`

## 🐳 Docker
To build and run with Docker:
```bash
mvn clean package
docker build -t ecommerce/config-server .
docker run -p 8888:8888 ecommerce/config-server
```

---

## 🎤 Interview Questions Related to this Module

**1. Why do we need a centralized Configuration Server in a microservices architecture?**
**Answer:** In a distributed system with dozens or hundreds of microservices, managing configuration files (like database credentials, Kafka brokers, and Eureka URLs) locally within each service becomes a nightmare. If a database password changes, you'd have to update and redeploy every single microservice. A centralized Config Server solves this by storing all configurations in one place. Services fetch their configuration at startup, ensuring consistency and making updates trivial.

**2. How does a Spring Boot application know where to find the Config Server?**
**Answer:** When a microservice starts, it looks for its configuration before initializing the main application context. The URL of the Config Server is defined in the microservice's `application.yml` (or `bootstrap.yml` in older versions) under `spring.config.import=optional:configserver:http://localhost:8888`.

**3. What happens if the Config Server is down when a microservice starts?**
**Answer:** By default, if a microservice cannot connect to the Config Server at startup, it will fail to start (fail-fast behavior). This prevents the service from starting with missing or incorrect default configurations. You can make the connection optional, but in production, fail-fast is preferred.

**4. How can we update configurations dynamically without restarting the microservices?**
**Answer:** Spring Cloud provides the `@RefreshScope` annotation. When a bean is annotated with `@RefreshScope`, it can be re-initialized at runtime. If a configuration changes in the Git/Native repo, you can trigger a refresh by calling the `/actuator/refresh` endpoint on the microservice. For a more automated approach across many services, you would use **Spring Cloud Bus** combined with Kafka/RabbitMQ to broadcast the configuration change event to all services simultaneously.

**5. How do you secure sensitive data like passwords in the Config Server?**
**Answer:** Storing plain text passwords in the `config-repo` is a security risk. You should encrypt sensitive properties. Spring Cloud Config supports symmetric or asymmetric encryption. You prefix the value with `{cipher}`. The Config Server decrypts the value before sending it to the client, or sends it encrypted and the client decrypts it. Alternatively, integration with a secrets manager like HashiCorp Vault or AWS Secrets Manager is highly recommended for production environments.