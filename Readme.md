
# Prerequisites

Make sure the following are installed:

- Java 17+
- Postgres DB
- Maven 3.8+
- Apache Kafka
- Kafka (KRaft mode)
- IntelliJ IDEA (recommended)

---
Kafka is used for **event-driven communication** between Topup and Account services.

---

# Step 1 — Run Kafka Locally
Go to directory where kafka is installed
```bash
cd kafka
bin/kafka-server-start.sh config/server.properties
```

# Step 2 - Create following databases
1. users
2. account
3. topup

# Step 3 - Run below services in order
1. discovery-server
2. api-gateway
3. auth-service
4. account-service
5. topup-service


# API endpoint and payload
Register user:
http://localhost:8080/auth/signup
```json
{
  "name": "Gagan",
  "email": "gagan@gmail.com",
  "password": "pass",
  "roles": [
    "ADMIN"
  ]
}
```

Login user
http://localhost:8080/auth/login
```json
{
  "email": "gagan@gmail.com",
  "password": "pass"
}
```

Create account
http://localhost:8080/accounts
```json
{
  "accountNumber": "ACC-1002",
  "ownerName": "Tulasha Basnet",
  "initialBalance": 2500.00
}
```

Topup:
http://localhost:8080/topup
```json
{
  "fromAccount": "ACC-1001",
  "toAccount": "ACC-1002",
  "amount": 250.00
}
```
