# Auth Service (auth_svc)

The **Auth Service** is a microservice responsible for authentication and authorization in a microservices ecosystem.
It handles **user registration, login, JWT issuance and refresh**, and publishes **user-related events** to Kafka for consumption by other services.
Built with **Spring Boot** and **Spring Security**, it ensures secure and scalable authentication.

---

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Prerequisites](#prerequisites)
* [Setup Instructions](#setup-instructions)
* [Configuration](#configuration)
* [API Endpoints](#api-endpoints)
* [Kafka Events](#kafka-events)
* [Running the Application](#running-the-application)
* [Testing](#testing)
* [Contributing](#contributing)
* [License](#license)

---

## Features

* **User Registration**: Securely register users with password hashing.
* **User Login**: Authenticate users and issue JWT access & refresh tokens.
* **Token Validation**: Validate JWTs for protected endpoints.
* **Token Refresh**: Generate new access tokens using refresh tokens.
* **Event Publishing**: Publish user-related events (e.g., `USER_REGISTERED`) to Kafka.
* **Database Storage**: Store user data in MySQL.
* **Security**: Leverage Spring Security for robust authentication and authorization.

---

## Tech Stack

* **Spring Boot 3.x** – Core framework for the microservice.
* **Spring Security 6** – Manages authentication and authorization.
* **Spring Data JPA / Hibernate** – Handles database operations.
* **MySQL** – Stores user data.
* **Spring Kafka** – Publishes events to Kafka.
* **JWT** – Enables token-based authentication.

---

## Prerequisites

Ensure the following are installed:

* **Java 17+** (required for Spring Boot 3.x)
* **Maven** (dependency management & build tool)
* **MySQL 8.x** (user data storage)
* **Kafka + ZooKeeper** (event streaming)
* **Docker** (optional, for containerized setup)
* **Git** (to clone the repository)

---

## Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-org/auth_svc.git
   cd auth_svc
   ```

2. **Set Up MySQL**

   * Create a database (e.g., `auth_db`).
   * Update `application.yml` with your database credentials.

3. **Set Up Kafka**

   * Run Kafka and ZooKeeper (locally or via Docker).
   * Example with Docker:

     ```bash
     docker-compose -f docker-compose.kafka.yml up -d
     ```

4. **Install Dependencies**

   ```bash
   mvn clean install
   ```

5. **Configure Environment**

   * Copy `application.example.yml` → `application.yml`.
   * Update with your settings (see [Configuration](#configuration)).

---

## Configuration

Example `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth_db?useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

jwt:
  secret: your_jwt_secret_key
  access-token-expiration: 3600000   # 1 hour
  refresh-token-expiration: 604800000 # 7 days

server:
  port: 8080
```

* **Database**: configure `spring.datasource` with MySQL credentials.
* **Kafka**: set `spring.kafka.bootstrap-servers` to your broker.
* **JWT**: use a secure `jwt.secret` for token signing.

---

## API Endpoints

| Method | Endpoint             | Description                                                   | Request Body Example                          |
| ------ | -------------------- | ------------------------------------------------------------- | --------------------------------------------- |
| POST   | `/api/auth/register` | Register a new user                                           | `{"username": "user", "password": "pass123"}` |
| POST   | `/api/auth/login`    | Authenticate and issue JWT tokens                             | `{"username": "user", "password": "pass123"}` |
| POST   | `/api/auth/refresh`  | Refresh access token                                          | `{"refreshToken": "your_refresh_token"}`      |
| GET    | `/api/auth/validate` | Validate JWT token (requires `Authorization: Bearer <token>`) | —                                             |

### Example Usage

**Register**

```bash
curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username": "john_doe", "password": "securePass123"}'
```

**Login**

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username": "john_doe", "password": "securePass123"}'
```

**Response**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

---

## Kafka Events

The service publishes events to the **`user-events`** Kafka topic.

Example event:

```json
{
  "eventType": "USER_REGISTERED",
  "userId": 1,
  "username": "john_doe",
  "timestamp": "2025-09-29T13:33:00+07:00"
}
```

➡️ Consumers should subscribe to the `user-events` topic.

---

## Running the Application

**Build**

```bash
mvn clean package
```

**Run**

```bash
mvn spring-boot:run
```

**Access**

* Service runs at [http://localhost:8080](http://localhost:8080).

**Docker (Optional)**

```bash
docker build -t auth_svc .
docker run -p 8080:8080 auth_svc
```

---

## Testing

* **Unit Tests**

  ```bash
  mvn test
  ```

* **Integration Tests**

  ```bash
  mvn verify
  ```

* **Manual Testing**
  Use Postman or cURL to test endpoints.

---

## Contributing

1. Fork the repository.
2. Create a feature branch:

   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit changes:

   ```bash
   git commit -m "Add your feature"
   ```
4. Push:

   ```bash
   git push origin feature/your-feature
   ```
5. Open a Pull Request.

Please ensure code follows project standards and includes tests.

---

## License

This project is licensed under the **MIT License**.
See [LICENSE](LICENSE) for details.
