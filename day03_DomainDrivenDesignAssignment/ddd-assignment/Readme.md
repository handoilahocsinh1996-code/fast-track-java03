# DDD Assignment

## Exercise 1 - Rich Domain Model

### Mục tiêu

Triển khai `Wallet` theo Rich Domain Model.

- `Wallet` là Aggregate Root.
- Encapsulate `balance` và `status`.
- Không sử dụng public setter.
- Implement `withdrawMoney()` và `lockWallet()`.
- Business rules:
  - Không withdraw khi Wallet bị `LOCKED`.
  - Không withdraw quá số dư.
  - Vi phạm rule → `DomainException`.

### Run Test

```powershell
.\mvnw.cmd clean test
```

---

## Exercise 2 - Logistics

### Mục tiêu

Triển khai tính phí vận chuyển sử dụng:

- Hexagonal Architecture
- Port & Adapter
- Strategy Pattern
- Factory Pattern
- OCP

### Run

```powershell
.\mvnw.cmd spring-boot:run
```

### Test API

**GHN**

```text
POST http://localhost:8080/shipping/fee?carrier=GHN
```

**GHTK**

```text
POST http://localhost:8080/shipping/fee?carrier=GHTK
```

Body:

```json
{
  "fromAddress": "Ha Noi",
  "toAddress": "Ho Chi Minh",
  "weight": 2
}
```

Kết quả:

```text
GHN: 30000
GHTK: 24000
```
