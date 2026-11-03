# Day 11 - Redis Leaderboard

## 1. Overview

Build a leaderboard REST API using Spring Boot, Redis Sorted Set (ZSET), and Hexagonal Architecture.

* Java 17
* Spring Boot 3.4.5
* Redis 7.4
* Docker Compose

## 2. Architecture

* **Domain:** Leaderboard model.
* **Application:** Use cases and business logic.
* **Adapters:** REST API and Redis repository.

## 3. Run the Project

Start Redis Master–Replica:

```bash
docker compose up -d
```

Run the Spring Boot application (Windows):

```powershell
.\mvnw.cmd spring-boot:run
```

## 4. API

**Update player score**

`POST /leaderboard/{username}`

```json
{
  "score": 150
}
```

**Get top N players**

`GET /leaderboard/top/{n}`

## 5. Run Tests

```powershell
.\mvnw.cmd test
```

Unit tests use Mockito and do not require a running Redis instance.

## 6. Redis Replication

* Master: `localhost:6379`
* Replica: `localhost:6380`

Redis replication is asynchronous by default.
