# Auth Service (auth_svc)

The **Auth Service** is responsible for **authentication** and **authorization** within the microservices ecosystem.  
It handles user registration, login, JWT issuance and refresh, and publishes events to Kafka so that other services can consume them.

---

## Features

- Register new users
- Login with JWT Access Token & Refresh Token
- Token validation for incoming requests
- Built with **Spring Security** & **JWT**
- Publishes user-related events to **Kafka** (e.g., `USER_REGISTERED`)
- Stores user data in **MySQL**

---

## Tech Stack

- **Spring Boot 3.x**
- **Spring Security 6**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Spring Kafka**
- **JWT (JSON Web Token)**
