# Authentication Service

The Authentication Service is the central authority for issuing JWTs and verifying user identities in the Distributed E-commerce Microservices architecture. It maintains its own independent database solely for storing credentials.

## 🚀 Technologies Used
- Java 21
- Spring Boot 3
- Spring Security
- JSON Web Tokens (jjwt)
- Spring Data JPA
- MySQL
- Hibernate

## ⚙️ Configuration
The service runs on port `8081`.
It registers with the Eureka Discovery Server and loads properties from the Config Server.

## 🛠️ How to Run
From the root directory:
```bash
cd auth-service
mvn spring-boot:run
```

## 🐳 Docker
To build and run with Docker (from root):
```bash
mvn clean package -pl auth-service -am -DskipTests
docker-compose up -d --build auth-service
```

---

## 🎤 Interview Questions Related to this Module

**1. Why is the Authentication Service separate from the User Service?**
**Answer:** Following Single Responsibility and Domain-Driven Design (DDD), authentication (verifying *who* you are) is fundamentally different from profile management (managing *what* your details are). By decoupling them, we can scale the heavily-hit Auth Service independently during traffic spikes (like a login surge) without scaling the entire User Service. It also minimizes the blast radius: if the User Service goes down, logins can potentially still process.

**2. How are passwords stored in the database?**
**Answer:** Passwords are never stored in plain text. They are hashed using **BCrypt** (`BCryptPasswordEncoder`). BCrypt is a computationally expensive, one-way cryptographic hash function that automatically incorporates a random "salt". This protects against rainbow table attacks and brute force cracking.

**3. What is the flow of JWT generation here?**
**Answer:**
1. Client sends `email` and `password` to `/api/auth/login`.
2. The `AuthenticationManager` uses the `CustomUserDetailsService` to fetch the stored `UserCredential` and compares the BCrypt hash.
3. If successful, the `JwtService` creates a signed JWT containing the user's email as the subject, along with an expiration time.
4. The service returns both an Access Token (short-lived) and a Refresh Token (long-lived) to the client.

**4. Why do we need both an Access Token and a Refresh Token?**
**Answer:** Security best practices dictate that Access Tokens should be short-lived (e.g., 15 minutes) to minimize the window of opportunity if the token is stolen. However, forcing a user to log in every 15 minutes is terrible UX. A Refresh Token is long-lived (e.g., 7 days) and stored securely. When the Access Token expires, the client uses the Refresh Token to request a new Access Token seamlessly behind the scenes.

**5. How is this service protected from unauthorized access if it issues tokens?**
**Answer:** The `SecurityConfig` defines an `AuthenticationFilterChain` that explicitly permits unauthenticated access *only* to `/api/auth/register`, `/api/auth/login`, and `/api/auth/validate`. Any other endpoint inside this service requires authentication. Additionally, the API Gateway filters traffic before it even reaches this service.