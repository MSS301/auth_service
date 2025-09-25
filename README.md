Auth Service (auth_svc)

The Auth Service is a core component of the microservices ecosystem.
It is responsible for authentication and authorization, ensuring that only verified users can access protected resources.

This service manages user registration, login, JWT token issuance and refresh, and communicates user-related events to other services via Kafka.

Features

User registration with validation

Secure login with username/email and password

Issues JWT Access Tokens and Refresh Tokens

Validates tokens for incoming requests

Stateless authentication using Spring Security

Publishes domain events (e.g., USER_REGISTERED) to Kafka

Persists user data in MySQL

Tech Stack

Spring Boot 3.x

Spring Security 6

Spring Data JPA / Hibernate

MySQL

Spring Kafka

JWT (JSON Web Token)
