# Banking Customer Service - Docker Setup Guide

## Docker Network

### List Networks

```bash
docker network ls
```

### Create Network

```bash
docker network create banking-network
```

---

## Docker Volume

### List Volumes

```bash
docker volume ls
```

### Create Volume

```bash
docker volume create customer-postgres-data
```

---

## PostgreSQL Container

### Run PostgreSQL

```bash
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

### Verify Running Containers

```bash
docker ps
```

### Access PostgreSQL

```bash
winpty docker exec -it customer-postgres psql -U postgres -d customerdb
```

---

## Spring Boot Build

### Generate JAR

```bash
mvn clean package -DskipTests
```

---

## Docker Image

### Build Image

```bash
docker build -t customer-service .
```

### List Images

```bash
docker images
```

---

## Customer Service Container

### Run Application

```bash
docker run -d \
  --name customer-service-app \
  --network banking-network \
  -p 8080:8080 \
  customer-service
```

### Verify Running Containers

```bash
docker ps
```

---

## Logs

### Customer Service Logs

```bash
docker logs -f customer-service-app
```

### PostgreSQL Logs

```bash
docker logs -f customer-postgres
```

---

## Container Management

### Stop Containers

```bash
docker stop customer-service-app
docker stop customer-postgres
```

### Start Containers

```bash
docker start customer-postgres
docker start customer-service-app
```

### Remove Containers

```bash
docker rm -f customer-service-app
docker rm -f customer-postgres
```

---

## Verification

### Check Running Containers

```bash
docker ps
```

### Open Application

```text
http://localhost:8080
```

---

## Cleanup Commands

### Remove Image

```bash
docker rmi customer-service
```

### Remove Volume

```bash
docker volume rm customer-postgres-data
```

### Remove Network

```bash
docker network rm banking-network
```

### List All Containers

```bash
docker ps -a
```

# CMD to Restart after the changes

```bash
mvn clean package -DshipTests
docker build -t customer-service .
docker rm -f customer-service-app
docker run -d --name customer-service-app --network banking-network -p 8080:8080 customer-service
docker logs -f customer-service-app
```
