# User Service

The User Service handles all business logic related to a customer's profile and shipping address. It is strictly separated from the Authentication Service to follow Domain-Driven Design (DDD) principles.

## 🚀 Technologies Used
- Java 21
- Spring Boot 3
- Spring Data JPA
- MySQL
- MapStruct

## ⚙️ Configuration
The service runs on port `8082`.
It registers with the Eureka Discovery Server and loads properties from the Config Server.
It maintains its own completely independent database schema (`ecommerce_user_db`).

## 🛠️ How to Run
From the root directory:
```bash
cd user-service
mvn spring-boot:run
```

## 🐳 Docker
To build and run with Docker (from root):
```bash
mvn clean package -pl user-service -am -DskipTests
docker-compose up -d --build user-service
```

---

## 🎤 Interview Questions Related to this Module

**1. Why use `MapStruct` instead of writing manual mappers or using `ModelMapper`?**
**Answer:** MapStruct is a compile-time code generator. Unlike reflection-based mappers (like ModelMapper or Dozer) which are slow and can cause runtime errors if properties mismatch, MapStruct generates plain Java code during the Maven build phase. This makes it incredibly fast, type-safe, and easy to debug because you can physically see the generated implementation class in the `target` folder.

**2. How did you secure the endpoints in the User Service without verifying the JWT signature again?**
**Answer:** In a distributed architecture, verifying the JWT signature at every single microservice is redundant and requires sharing the secret key across all services, expanding the attack surface. Instead, the **API Gateway** acts as the perimeter. The Gateway verifies the JWT, extracts the user's identity, and forwards it to the User Service via a custom HTTP header (`loggedInUser`). The User Service uses a lightweight `OncePerRequestFilter` to read this header and establish a Spring `SecurityContext`, implicitly trusting the internal network.

**3. Why use `@OneToOne(fetch = FetchType.LAZY)` on the User and Address relationship?**
**Answer:** By default, JPA loads `@OneToOne` associations eagerly. If we fetch a list of Users, Hibernate will execute a separate query to fetch the Address for every single user (the N+1 query problem). Using `LAZY` fetching ensures the Address is only loaded from the database when we explicitly call `user.getAddress()`, saving significant database overhead.

**4. If the Auth Service creates the credentials, how does the User Service know about the user?**
**Answer:** In this architecture, when a user logs in for the first time and tries to `PUT /api/users/profile`, the User Service attempts to find the user by their email (forwarded by the Gateway). If the profile doesn't exist, it creates an empty profile record on the fly and updates it. In a fully mature Event-Driven Architecture, the Auth Service would publish a `UserRegisteredEvent` to Kafka, and the User Service would consume it to pre-create the profile asynchronously.