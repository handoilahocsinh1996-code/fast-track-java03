# Day 12 - Multi-level Caching

## 1. Technologies

- Java 17, Spring Boot 3.4.5
- Hexagonal Architecture
- Caffeine (L1 Cache)
- Redis (L2 Cache, Pub/Sub)
- MySQL
- JUnit 5, Mockito

## 2. Cache Flow

- **L1 Hit:** Trả dữ liệu từ Caffeine.
- **L1 Miss:** Kiểm tra Redis L2.
- **L2 Miss:** Truy vấn MySQL và nạp dữ liệu vào cache.
- **Cache Invalidation:** Redis Pub/Sub thông báo cho các instance xóa L1 khi sản phẩm bị xóa.

## 3. Run Project

Start MySQL and Redis:

```powershell
docker compose up -d
```

Run application:

```powershell
.\mvnw.cmd spring-boot:run
```

Run tests:

```powershell
.\mvnw.cmd test
```

## 4. APIs

| Method | Endpoint             | Description    |
| ------ | -------------------- | -------------- |
| POST   | `/api/products`      | Create product |
| GET    | `/api/products/{id}` | Get product    |
| DELETE | `/api/products/{id}` | Delete product |

## 5. Architecture

Client → Controller → Use Case → Service → Cache / Repository → Redis / MySQL

Caffeine is the local cache; Redis is the shared cache between instances.
