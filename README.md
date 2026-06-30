# 🛒 Distributed E-commerce Microservices

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Kafka](https://img.shields.io/badge/Apache-Kafka-black)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![License](https://img.shields.io/badge/License-MIT-green)

A cloud-native e-commerce backend built using **Spring Boot Microservices**, **Apache Kafka**, **Docker**, and **MySQL**. The project follows a distributed architecture where each business capability is implemented as an independent service communicating through REST APIs and asynchronous events.

---

## 📌 Overview

This project demonstrates modern backend development practices by implementing an event-driven microservices architecture for an e-commerce platform.

Each service owns its own database, exposing REST APIs while publishing and consuming events through **Apache Kafka** to enable loose coupling and scalability.

---

## 🚀 Tech Stack

| Category | Technologies |
|----------|--------------|
| Language | Java 21 |
| Framework | Spring Boot, Spring Data JPA |
| Database | MySQL |
| Messaging | Apache Kafka |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |
| Version Control | Git & GitHub |

---

# 🏗️ Architecture

```text
                    Client
                       │
              REST API Requests
                       │
      ┌──────────────────────────────────┐
      │                                  │
 User Service                 Product Service
      │                                  │
      └──────────────┬───────────────────┘
                     │
                 Kafka Events
                     │
      ┌──────────────┴───────────────────┐
      │                                  │
Order Service                 Inventory Service
      │                                  │
      └──────────────┬───────────────────┘
                     │
                  MySQL Databases
```

---

# 📦 Microservices

### 👤 User Service

- User Registration
- Login
- Profile Management

### 📦 Product Service

- Product CRUD
- Category Management
- Inventory Lookup

### 🛍️ Order Service

- Create Orders
- Order History
- Payment Status

### 📊 Inventory Service

- Stock Management
- Inventory Updates
- Low Stock Notifications

---

# ✨ Features

- RESTful Microservices
- Event-Driven Communication with Kafka
- Independent Service Deployment
- Dockerized Development Environment
- Database per Service Pattern
- Modular Project Structure
- Scalable Architecture
- Clean Layered Design

---

# 📁 Project Structure

```text
distributed-ecommerce/

├── user-service/
├── product-service/
├── inventory-service/
├── order-service/
├── docker-compose.yml
└── README.md
```

---

# ⚙️ Getting Started

## Clone Repository

```bash
git clone https://github.com/Palak-1438/distributed-ecommerce-microservices.git

cd distributed-ecommerce-microservices
```

---

## Start Kafka & MySQL

```bash
docker-compose up -d
```

---

## Run Individual Services

```bash
cd user-service
mvn spring-boot:run
```

Repeat for the remaining services.

---

# 🔄 Event Flow

```text
Customer places an order

        │

        ▼

Order Service

        │

Publishes Event

        │

        ▼

Apache Kafka

        │

        ▼

Inventory Service

        │

Updates Stock

        │

        ▼

Order Completed
```

---

# 📡 Sample API Endpoints

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/users/register` | Register User |
| POST | `/users/login` | Login User |
| GET | `/products` | List Products |
| POST | `/orders` | Create Order |
| GET | `/orders/{id}` | Get Order Details |

---

# 🧪 Future Enhancements

- API Gateway
- Service Discovery (Eureka)
- Config Server
- JWT Authentication
- Redis Caching
- Circuit Breaker (Resilience4j)
- Kubernetes Deployment
- Prometheus & Grafana Monitoring

---

# 📷 Screenshots

> Screenshots will be added after the UI and services are fully implemented.

---

# 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a Pull Request

---

# 👩‍💻 Author

**Palak Dusiya**

- GitHub: https://github.com/Palak-1438
- LinkedIn: https://linkedin.com/in/palak-dusiya

---

# 📄 License

This project is licensed under the MIT License.
