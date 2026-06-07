```bash
mvn clean package -DskipTests
docker build -t account-service .
docker rm -f account-service-app
docker run -d --name account-service-app --network banking-network -p 8081:8081 account-service
docker logs -f account-service-app
```
