# Eureka Discovery Server

This module acts as the service registry for the Distributed E-commerce Microservices architecture. It allows all other microservices to register themselves and discover each other dynamically.

## 🚀 Technologies Used
- Java 21
- Spring Boot 3
- Spring Cloud Netflix Eureka Server

## ⚙️ Configuration
The server runs on port `8761`. Since it is the discovery server itself, it is configured not to register with Eureka (`eureka.client.register-with-eureka=false`).

## 🛠️ How to Run
From the root directory:
```bash
cd eureka-discovery-server
mvn spring-boot:run
```
Once started, you can view the Eureka dashboard at `http://localhost:8761`.

## 🐳 Docker
To build and run with Docker:
```bash
mvn clean package
docker build -t ecommerce/discovery-server .
docker run -p 8761:8761 ecommerce/discovery-server
```

---

## 🎤 Interview Questions Related to this Module

**1. What is Service Discovery and why is it needed in microservices?**
**Answer:** Service Discovery is the process of automatically detecting devices and services on a network. In microservices, services often have dynamic IP addresses due to autoscaling, containerization, and failures. Hardcoding IP addresses is impractical. A discovery server acts as a central registry where services register their locations, allowing other services to find and communicate with them dynamically.

**2. How does Spring Cloud Netflix Eureka work?**
**Answer:** Eureka consists of a server (registry) and clients (microservices).
- **Registration:** When a client starts, it registers its metadata (IP, port, health indicator URL) with the Eureka server.
- **Heartbeats:** Clients send periodic heartbeats (default every 30s) to the server. If the server doesn't receive a heartbeat within a certain threshold (default 90s), it removes the instance from its registry.
- **Discovery:** Other clients fetch the registry from the server to locate and communicate with the registered instances, often using a load balancer (like Spring Cloud LoadBalancer) to distribute requests.

**3. What is the difference between client-side and server-side discovery?**
**Answer:**
- **Client-Side Discovery (e.g., Eureka):** The client queries the service registry to find the locations of available service instances and then uses a load balancing algorithm to select one and make the request directly.
- **Server-Side Discovery (e.g., AWS ELB, Kubernetes Services):** The client makes a request to a router/load balancer. The load balancer queries the service registry and routes the request to an available instance.

**4. Why did we set `register-with-eureka=false` and `fetch-registry=false` in the application properties?**
**Answer:** By default, the Eureka Server also acts as a Eureka Client. Since this application is a standalone registry (not part of a cluster in this configuration), we set these properties to `false` to prevent it from trying to register itself with another Eureka Server and from trying to fetch a registry that doesn't exist elsewhere, which would cause startup errors or unnecessary log noise.

**5. How would you make the Eureka Discovery Server highly available?**
**Answer:** In production, a single Eureka server is a single point of failure. High availability is achieved by running multiple Eureka Server instances (a cluster) and configuring them to peer with each other. They replicate their registries to stay synchronized. In `application.yml`, you would point the `service-url.defaultZone` to the other peer instances, and set `register-with-eureka` and `fetch-registry` to `true` (or leave default).
