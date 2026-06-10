# 🏦 Banking System

A microservices-based Banking System built with Spring Boot, PostgreSQL, Docker, and gRPC.

## Architecture

| Service             | Application Port | Database Port (Host) | Database Port (Container) |
| ------------------- | ---------------- | -------------------- | ------------------------- |
| Customer Service    | 8080             | 5433                 | 5432                      |
| Account Service     | 8081             | 5434                 | 5432                      |
| Transaction Service | 8082             | 5435                 | 5432                      |

### Service Communication

```text
Customer Service (gRPC Server : 9090)
            │
            │ gRPC
            ▼
     Account Service
            │ gRPC
            ▼
   Transaction Service
```

- Customer Service exposes a gRPC endpoint for customer validation.
- Account Service validates customer existence before account creation.
- Each service owns its own PostgreSQL database.

---

# Prerequisites

- Java 21+
- Maven 3.9+
- Docker
- Docker Desktop (Windows/Mac) or Docker Engine (Linux)

---

# Infrastructure Setup

## Create Docker Network

```bash
docker network create banking-network
```

---

# Database Setup

## Customer Database

```bash
docker volume create customer-postgres-data

docker run -d \
  --name customer-postgres \
  --network banking-network \
  -v customer-postgres-data:/var/lib/postgresql/data \
  -e POSTGRES_DB=customerdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres123 \
  -p 5433:5432 \
  postgres:15
```

## Account Database

```bash
docker volume create account-postgres-data

docker run -d \
  --name account-postgres \
  --network banking-network \
  -v account-postgres-data:/var/lib/postgresql/data \
  -e POSTGRES_DB=accountdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres123 \
  -p 5434:5432 \
  postgres:15
```

## Transaction Database

```bash
docker volume create transaction-postgres-data

docker run -d \
  --name transaction-postgres \
  --network banking-network \
  -v transaction-postgres-data:/var/lib/postgresql/data \
  -e POSTGRES_DB=transactiondb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres123 \
  -p 5435:5432 \
  postgres:15
```

---

# Build Services

Run the following command from each service directory:

```bash
mvn clean package -DskipTests
```

---

# Build Docker Images

## Customer Service

```bash
docker build -t customer-service .
```

## Account Service

```bash
docker build -t account-service .
```

## Transaction Service

```bash
docker build -t transaction-service .
```

---

# Run Services

## Customer Service

```bash
docker run -d \
  --name customer-service-app \
  --network banking-network \
  -p 8080:8080 \
  -p 9090:9090 \
  customer-service
```

## Account Service

```bash
docker run -d \
  --name account-service-app \
  --network banking-network \
  -p 8081:8081 \
  account-service
```

## Transaction Service

```bash
docker run -d \
  --name transaction-service-app \
  --network banking-network \
  -p 8082:8082 \

  transaction-service
```

---

# Verification

Verify all containers are running:

```bash
docker ps
```

### Service Endpoints

| Service                 | URL                   |
| ----------------------- | --------------------- |
| Customer Service-app    | http://localhost:8080 |
| Account Service-app     | http://localhost:8081 |
| Transaction Service-app | http://localhost:8082 |

---

# Viewing Logs

```bash
docker logs -f customer-service-app
docker logs -f account-service-app
docker logs -f transaction-service-app
```

---

# Development Workflow

After making code changes:

```bash
mvn clean package -DskipTests
docker build -t <service-name> .
docker rm -f <container-name>
docker run -d --name <container-name> --network banking-network -p <host-port>:<container-port> <service-name>
```

---

# Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- gRPC
- Docker
- Maven

---

# Future Enhancements

- API Gateway
- Service Discovery (Eureka)
- Centralized Configuration
- Distributed Tracing
- Kafka Integration
- JWT Authentication & Authorization
- CI/CD Pipeline
