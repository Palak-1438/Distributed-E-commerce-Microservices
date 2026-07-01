# API Gateway

The API Gateway acts as the single point of entry for all client requests into the Distributed E-commerce Microservices architecture. It routes requests to the appropriate downstream microservices, handles cross-cutting concerns (like security and CORS), and implements resilience patterns.

## 🚀 Technologies Used
- Java 21
- Spring Boot 3
- Spring Cloud Gateway (WebFlux based)
- Spring Security & JWT
- Resilience4j (Circuit Breaker)
- Redis (Reactive Rate Limiting)

## ⚙️ Configuration
The Gateway runs on port `8080`.
It fetches its routing rules and configurations dynamically from the central Config Server (`config-repo/api-gateway.yml`).

## 🛠️ How to Run
From the root directory:
```bash
cd api-gateway
mvn spring-boot:run
```

## 🐳 Docker
To build and run with Docker (from root):
```bash
mvn clean package -pl api-gateway -am -DskipTests
docker-compose up -d --build api-gateway
```

---

## 🎤 Interview Questions Related to this Module

**1. Why use an API Gateway instead of letting clients connect to microservices directly?**
**Answer:** An API Gateway provides several critical benefits:
- **Abstraction:** Clients only need to know one URL, regardless of how many microservices are behind it.
- **Cross-Cutting Concerns:** It centralizes logic like authentication, CORS, rate limiting, and SSL termination, preventing code duplication across services.
- **Protocol Translation:** It can translate public protocols (like REST/HTTP) to internal ones (like gRPC) if needed.
- **Resilience:** It can act as a circuit breaker, preventing cascading failures if a downstream service goes down.

**2. Why is Spring Cloud Gateway built on WebFlux (Project Reactor) instead of standard Spring Web MVC?**
**Answer:** An API Gateway typically handles a massive number of concurrent requests, mostly just passing them through (I/O bound). Traditional thread-per-request models (like Spring MVC/Tomcat) quickly run out of threads and memory under high load. WebFlux uses a reactive, non-blocking, event-driven model (via Netty) that can handle tens of thousands of concurrent connections using very few threads, making it highly scalable.

**3. How did you implement security at the Gateway level?**
**Answer:** I implemented a custom `GatewayFilterFactory` (`JwtAuthenticationFilter`). When a request comes in, the filter checks if the route is secured. If it is, it extracts the `Authorization: Bearer <token>` header, parses and validates the JWT using the `jjwt` library and the shared secret. If the token is valid, it allows the request to pass through; otherwise, it immediately returns a `401 Unauthorized`.

**4. What is the Circuit Breaker pattern and how is it used in the Gateway?**
**Answer:** The Circuit Breaker pattern prevents an application from repeatedly trying to execute an operation that's likely to fail. In the Gateway, I used Resilience4j. If a downstream service (like `user-service`) becomes unresponsive, the circuit breaker trips from CLOSED to OPEN after a certain failure threshold. While OPEN, the Gateway stops routing requests to the dead service and instead immediately returns a fallback response (via `FallbackController`), keeping the gateway itself responsive and preventing thread exhaustion.

**5. How are dynamic routes managed in this setup?**
**Answer:** The routes are defined in the centralized `api-gateway.yml` served by the Config Server. We use `lb://service-name` for the URI, which tells Spring Cloud Gateway to use the Eureka Service Registry (via Spring Cloud LoadBalancer) to dynamically resolve the physical IP and port of the requested microservice, load balancing across multiple instances if they exist.