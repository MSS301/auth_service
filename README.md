Auth Service (auth_svc)

The Auth Service is a microservice responsible for authentication and authorization in a microservices ecosystem. It handles user registration, login, JWT (JSON Web Token) issuance and refresh, and publishes user-related events to Kafka for consumption by other services. Built with Spring Boot and Spring Security, it ensures secure and scalable authentication.

Table of Contents





Features



Tech Stack



Prerequisites



Setup Instructions



Configuration



API Endpoints



Kafka Events



Running the Application



Testing



Contributing



License

Features





User Registration: Securely register users with password hashing.



User Login: Authenticate users and issue JWT access and refresh tokens.



Token Validation: Validate JWTs for protected endpoints.



Token Refresh: Generate new access tokens using refresh tokens.



Event Publishing: Publish events (e.g., USER_REGISTERED) to Kafka.



Database Storage: Store user data in MySQL.



Security: Leverage Spring Security for robust authentication and authorization.

Tech Stack





Spring Boot 3.x: Core framework for the microservice.



Spring Security 6: Manages authentication and authorization.



Spring Data JPA / Hibernate: Handles database operations.



MySQL: Stores user data.



Spring Kafka: Publishes events to Kafka.



JWT: Enables token-based authentication.

Prerequisites

Ensure the following are installed:





Java 17+: Required for Spring Boot 3.x.



Maven: For dependency management and building.



MySQL 8.x: For user data storage.



Kafka: With ZooKeeper for event streaming.



Docker (optional): For containerized setup.



Git: To clone the repository.

Setup Instructions





Clone the Repository:

git clone https://github.com/your-org/auth_svc.git
cd auth_svc



Set Up MySQL:





Create a database (e.g., auth_db).



Update application.yml with your database credentials.



Set Up Kafka:





Run Kafka and ZooKeeper (locally or via Docker).



Example with Docker:

docker-compose -f docker-compose.kafka.yml up -d



Install Dependencies:

mvn clean install



Configure Environment:





Copy application.example.yml to application.yml.



Update with your settings (see Configuration).

Configuration

Configure the service in application.yml. Example:

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
  access-token-expiration: 3600000 # 1 hour
  refresh-token-expiration: 604800000 # 7 days

server:
  port: 8080





Database: Set spring.datasource with MySQL credentials.



Kafka: Configure spring.kafka.bootstrap-servers for your Kafka broker.



JWT: Use a secure jwt.secret for token signing.

API Endpoints

The service provides RESTful endpoints for authentication:







Method



Endpoint



Description



Request Body Example





POST



/api/auth/register



Register a new user



{"username": "user", "password": "pass123"}





POST



/api/auth/login



Authenticate and issue tokens



{"username": "user", "password": "pass123"}





POST



/api/auth/refresh



Refresh access token



{"refreshToken": "your_refresh_token"}





GET



/api/auth/validate



Validate JWT token



(Requires Authorization: Bearer <token> header)

Example Usage





Register:

curl -X POST http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username": "john_doe", "password": "securePass123"}'



Login:

curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username": "john_doe", "password": "securePass123"}'

Response:

{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}

Kafka Events

The service publishes events to the user-events Kafka topic. Example event:





USER_REGISTERED:

{
  "eventType": "USER_REGISTERED",
  "userId": 1,
  "username": "john_doe",
  "timestamp": "2025-09-29T13:33:00+07:00"
}

Consumers should subscribe to the user-events topic.

Running the Application





Build:

mvn clean package



Run:

mvn spring-boot:run



Access:





Service runs at http://localhost:8080.



Docker (Optional):

docker build -t auth_svc .
docker run -p 8080:8080 auth_svc

Testing





Unit Tests:

mvn test



Integration Tests:

mvn verify



Manual Testing:





Use Postman or cURL to test endpoints.

Contributing





Fork the repository.



Create a branch: git checkout -b feature/your-feature.



Commit changes: git commit -m "Add your feature".



Push: git push origin feature/your-feature.



Open a Pull Request.

Ensure code follows project standards and includes tests.

License

MIT License. See LICENSE for details.
