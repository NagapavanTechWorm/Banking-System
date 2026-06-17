# 🏦 Banking System

A microservices-based Banking System built with Spring Boot, PostgreSQL, Docker, gRPC, JWT Authentication, and API Gateway.

---

# Architecture

## Services

| Service             | Application Port | gRPC Port | Database Port (Host) |
| ------------------- | ---------------- | --------- | -------------------- |
| Customer Service    | 8080             | 9090      | 5433                 |
| Account Service     | 8081             | 9091      | 5434                 |
| Transaction Service | 8082             | -         | 5435                 |
| Auth Service        | 8083             | -         | 5436                 |
| API Gateway         | 4000             | -         | -                    |

---

## Microservice Communication

```text
Client
   │
   ▼
API Gateway (4000)
   │
   ├── Auth Service (8083)
   │
   ├── Customer Service (8080)
   │         ▲
   │         │ gRPC : 9090
   │         │
   ├── Account Service (8081)
   │         ▲
   │         │ gRPC : 9091
   │         │
   └── Transaction Service (8082)
```

### gRPC Communication Flow

```text
Customer Service
      ▲
      │ Customer Validation
      │ (gRPC : 9090)
      │
Account Service
      ▲
      │ Account Validation
      │ (gRPC : 9091)
      │
Transaction Service
```

### Service Responsibilities

#### Customer Service

- Manages customer information.
- Owns the Customer Database.
- Exposes a gRPC server on port `9090`.
- Provides customer validation to Account Service.

#### Account Service

- Manages bank accounts.
- Owns the Account Database.
- Acts as a **gRPC Client** when communicating with Customer Service.
- Validates customer existence before account creation.
- Exposes a **gRPC Server** on port `9091`.
- Provides account validation to Transaction Service.

#### Transaction Service

- Manages deposits, withdrawals, and fund transfers.
- Owns the Transaction Database.
- Acts as a **gRPC Client** when communicating with Account Service.
- Validates account existence before processing transactions.

#### Auth Service

- Handles authentication and authorization.
- Generates and validates JWT tokens.

#### API Gateway

- Single entry point for all client requests.
- Routes requests to backend services.
- Applies JWT validation before forwarding requests.

---

# Technology Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring Cloud Gateway
- PostgreSQL
- gRPC
- JWT Authentication
- Docker
- Docker Compose
- Maven

---

# Prerequisites

- Java 21+
- Maven 3.9+
- Docker
- Docker Compose

---

# Project Structure

```text
banking-system/
│
├── customer-service
├── account-service
├── transaction-service
├── auth-service
├── api-gateway
│
└── docker-compose.yml
```

---

# Database Architecture

Each service owns its own database.

| Service             | Database      |
| ------------------- | ------------- |
| Customer Service    | customerdb    |
| Account Service     | accountdb     |
| Transaction Service | transactiondb |
| Auth Service        | authdb        |

This follows the Database-per-Service pattern.

---

# Running the Application

## Build All Services

Run from each service directory:

```bash
mvn clean package -DskipTests
```

---

## Start Entire System

```bash
docker compose up --build -d
```

---

## Verify Running Containers

```bash
docker compose ps
```

Expected Containers:

```text
customer-postgres
account-postgres
transaction-postgres
auth-postgres

customer-service-app
account-service-app
transaction-service-app
auth-service-app

api-gateway-app
```

---

# API Gateway

Base URL:

```text
http://localhost:4000
```

All external requests must go through API Gateway.

---

# Authentication APIs

## Login

```http
POST /auth/login
```

## Validate

```http
GET /auth/validate
```

Response:

```json
{
  "token": "jwt-token"
}
```

---

# Customer APIs

```http
GET    /api/customers
GET    /api/customers/{id}
POST   /api/customers
PUT    /api/customers/{id}
DELETE /api/customers/{id}
```

Authorization Header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# Account APIs

```http
POST /api/accounts
POST /api/accounts/balance
```

### Account Creation Flow

```text
Create Account
      │
      ▼
Account Service
      │
      ▼
gRPC Call
      │
      ▼
Customer Service
      │
      ▼
Validate Customer
      │
      ▼
Create Account
```

Authorization Header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# Transaction APIs

```http
GET  /api/transactions/{id}
POST /api/transactions/deposit
POST /api/transactions/withdraw
POST /api/transactions/transfer
```

### Transaction Processing Flow

```text
Transaction Request
        │
        ▼
Transaction Service
        │
        ▼
gRPC Call
        │
        ▼
Account Service
        │
        ▼
Validate Account
        │
        ▼
Process Transaction
```

Authorization Header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# JWT Authentication Flow

```text
Client
   │
   ▼
Login Request
   │
   ▼
Auth Service
   │
   ▼
JWT Generated
   │
   ▼
Client Stores Token
   │
   ▼
API Gateway
   │
   ▼
Token Validation
   │
   ▼
Target Service
```

---

# API Gateway Routes

| Route                  | Destination         |
| ---------------------- | ------------------- |
| /auth/\*\*             | Auth Service        |
| /api/customers/\*\*    | Customer Service    |
| /api/accounts/\*\*     | Account Service     |
| /api/transactions/\*\* | Transaction Service |

---

# Service-to-Service Communication

| Source Service      | Destination Service | Protocol |
| ------------------- | ------------------- | -------- |
| Account Service     | Customer Service    | gRPC     |
| Transaction Service | Account Service     | gRPC     |
| API Gateway         | All Services        | HTTP     |

---

# Docker Commands

## Build and Start

```bash
docker compose up --build -d
```

## Start Existing Containers

```bash
docker compose start
```

## Stop Containers

```bash
docker compose stop
```

## Restart Containers

```bash
docker compose restart
```

## View Running Containers

```bash
docker compose ps
```

## View Logs

```bash
docker compose logs -f
```

### Specific Service Logs

```bash
docker compose logs -f customer-service
docker compose logs -f account-service
docker compose logs -f transaction-service
docker compose logs -f auth-service
docker compose logs -f api-gateway
```

## Remove Containers

```bash
docker compose down
```

## Remove Containers and Volumes

```bash
docker compose down -v
```

> Warning: This command permanently deletes all PostgreSQL data.

---

# Development Workflow

After code changes:

```bash
mvn clean package -DskipTests
docker compose up --build -d
```

Rebuild a specific service:

```bash
docker compose up --build -d customer-service
docker compose up --build -d account-service
docker compose up --build -d transaction-service
docker compose up --build -d auth-service
docker compose up --build -d api-gateway
```

---

# Future Enhancements

- Service Discovery using Eureka
- Centralized Configuration Server
- Distributed Tracing (Zipkin / OpenTelemetry)
- Kafka Event Streaming
- Resilience4j Circuit Breakers
- Prometheus Monitoring
- Grafana Dashboards
- CI/CD Pipeline using GitHub Actions
- Kubernetes Deployment
- Role-Based Access Control (RBAC)

---

# Author

Banking System — A production-style microservices architecture demonstrating Spring Boot, PostgreSQL, Docker, gRPC, JWT Authentication, and API Gateway integration.
