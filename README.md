Auth Service (auth_svc)
The Auth Service is a core component of a microservices ecosystem, responsible for handling authentication and authorization. It provides secure user registration, login, JWT (JSON Web Token) issuance and refresh, and publishes user-related events to Kafka for consumption by other services. Built with Spring Boot and Spring Security, it ensures robust security and seamless integration with a MySQL database and Kafka message broker.
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

User Registration: Register new users with secure password hashing.
User Login: Authenticate users and issue JWT access and refresh tokens.
Token Validation: Validate incoming JWTs for protected endpoints.
Token Refresh: Issue new access tokens using refresh tokens.
Event Publishing: Publishes user-related events (e.g., USER_REGISTERED) to Kafka.
Database Integration: Stores user data in a MySQL database.
Secure Authentication: Leverages Spring Security for robust authentication and authorization.

Tech Stack

Spring Boot 3.x: Framework for building the microservice.
Spring Security 6: Handles authentication and authorization.
Spring Data JPA / Hibernate: For database operations with MySQL.
MySQL: Relational database for storing user data.
Spring Kafka: Publishes events to Kafka topics.
JWT (JSON Web Token): For secure token-based authentication.

Prerequisites
Before setting up the Auth Service, ensure you have the following installed:

Java 17 or later (Spring Boot 3.x requirement)
Maven (for dependency management and building)
MySQL (version 8.x recommended)
Kafka (with ZooKeeper)
Docker (optional, for containerized setup)
Git (to clone the repository)

Setup Instructions

Clone the Repository:
git clone https://github.com/your-org/auth_svc.git
cd auth_svc


Set Up MySQL:

Create a MySQL database (e.g., auth_db).
Update the application.yml file with your database credentials (see Configuration).


Set Up Kafka:

Ensure Kafka and ZooKeeper are running (locally or via Docker).
Example using Docker:docker-compose -f docker-compose.kafka.yml up -d




Install Dependencies:

Run the following to resolve dependencies:mvn clean install




Configure Environment:

Copy application.example.yml to application.yml and update with your configurations (see Configuration).



Configuration
The configuration is managed via application.yml. Below is an example configuration:
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth_db
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
  access-token-expiration: 3600000  # 1 hour in milliseconds
  refresh-token-expiration: 604800000  # 7 days in milliseconds

server:
  port: 8080


Database: Update spring.datasource with your MySQL credentials.
Kafka: Ensure spring.kafka.bootstrap-servers points to your Kafka broker.
JWT: Replace jwt.secret with a secure key for signing tokens.

API Endpoints
The Auth Service exposes RESTful endpoints for authentication and authorization. Below are the primary endpoints:



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
Authenticate user and issue tokens
{"username": "user", "password": "pass123"}


POST
/api/auth/refresh
Refresh access token using refresh token
{"refreshToken": "your_refresh_token"}


GET
/api/auth/validate
Validate a JWT token
(Requires Authorization: Bearer <token> header)


Example Usage

Register a User:
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
The Auth Service publishes events to Kafka for other services to consume. Key events include:

USER_REGISTERED: Published when a new user registers.
Topic: user-events
Payload Example:{
  "eventType": "USER_REGISTERED",
  "userId": 1,
  "username": "john_doe",
  "timestamp": "2025-09-29T13:28:00Z"
}





To consume these events, configure your Kafka consumer to listen to the user-events topic.
Running the Application

Build the Project:
mvn clean package


Run the Application:
mvn spring-boot:run


Access the Application:

The service will be available at http://localhost:8080.


Docker (Optional):

Build and run the service using Docker:docker build -t auth_svc .
docker run -p 8080:8080 auth_svc





Testing

Unit Tests:

Run unit tests using:mvn test




Integration Tests:

Ensure MySQL and Kafka are running, then execute:mvn verify




Manual Testing:

Use tools like Postman or cURL to test the API endpoints (see API Endpoints).



Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a feature branch (git checkout -b feature/your-feature).
Commit your changes (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
Open a Pull Request.

Please ensure your code follows the project's coding standards and includes tests.
License
This project is licensed under the MIT License. See the LICENSE file for details.
