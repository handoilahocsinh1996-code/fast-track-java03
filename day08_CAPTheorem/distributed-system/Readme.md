# Bài tập: Thiết kế Distributed System

Bài tập đáp ứng 5 yêu cầu:

| Yêu cầu              | Kết quả                                      |
| -------------------- | -------------------------------------------- |
| Chọn DB              | MySQL                                        |
| Chọn CAP Strategy    | CP                                           |
| Justify choice       | Ưu tiên Consistency cho hệ thống chuyển tiền |
| Architecture Diagram | Client → Gateway → Application Nodes → MySQL |
| Bonus API            | Create Account + Transfer Money              |

## Quyết định CAP Strategy

### Lựa chọn: CP

CP gồm:

- **C - Consistency (Tính nhất quán)**
- **P - Partition Tolerance (Khả năng chịu phân vùng mạng)**

Hệ thống ưu tiên **Consistency** khi xảy ra lỗi mạng giữa các node.

Ví dụ:

```text
Tài khoản A: 1.000.000
Tài khoản B:   500.000
```

A chuyển 200.000 cho B:

```text
Tài khoản A:   800.000
Tài khoản B:   700.000
```

Hệ thống không nên để xảy ra trạng thái:

```text
Tài khoản A:   800.000
Tài khoản B:   500.000
```

vì tiền đã bị trừ ở A nhưng chưa được cộng cho B.

---

# Architecture Diagram

Kiến trúc hệ thống:

```text
                         Client
                            |
                            v
                     +-------------+
                     | API Gateway |
                     +------+------+
                            |
                 +----------+----------+
                 |                     |
                 v                     v
        +----------------+    +----------------+
        | Application    |    | Application    |
        |    Node 1      |    |    Node 2      |
        +-------+--------+    +-------+--------+
                |                     |
                +----------+----------+
                           |
                           v
                    +-------------+
                    |    MySQL    |
                    +-------------+
```

# Bonus: Implement Small API

Hệ thống cung cấp 2 API đơn giản để demo.

### API 1: Tạo tài khoản

```http
POST /accounts
```

Request:

```json
{
  "ownerName": "Nguyen Van A",
  "initialBalance": 1000000
}
```

---

### API 2: Chuyển tiền

```http
POST /accounts/{id}/transfer
```

Request:

```json
{
  "toAccountId": 2,
  "amount": 200000
}
```

Ví dụ:

```text
Trước:

Account A = 1.000.000
Account B =   500.000


A chuyển 200.000 cho B


Sau:

Account A =   800.000
Account B =   700.000
```

API chuyển tiền sử dụng Transaction để đảm bảo thao tác trừ tiền và cộng tiền được xử lý nhất quán.

---

## Hướng dẫn chạy project

### Yêu cầu

- Java 17
- MySQL

### Tạo database

```sql
CREATE DATABASE distributed_system;
```

Cấu hình MySQL trong `src/main/resources/application.properties`.

### Chạy project

```bash
.\mvnw.cmd spring-boot:run
```

Application chạy tại:

```text
http://localhost:8080
```

### Test API

Tạo account:

```text
POST /accounts
```

Chuyển tiền:

```text
POST /accounts/{id}/transfer
```
