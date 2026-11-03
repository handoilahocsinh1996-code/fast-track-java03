# Inventory Concurrency

## 1. Giới thiệu

Project mô phỏng hệ thống quản lý tồn kho, tập trung vào kiểm soát truy cập đồng thời, xử lý xung đột cập nhật và lưu lịch sử thay đổi dữ liệu.

**Công nghệ sử dụng**

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA / Hibernate
- MySQL
- Hibernate Envers
- JUnit 5, Mockito và MockMvc
- Hexagonal Architecture

## 2. Chức năng chính

- Nhập thêm hàng bằng **Pessimistic Locking**.
- Cập nhật tồn kho bằng **Optimistic Locking** với `@Version`.
- Lưu và truy vấn lịch sử thay đổi bằng Hibernate Envers.
- Kiểm tra dữ liệu đầu vào và xử lý lỗi thông qua `GlobalExceptionHandler`.

## 3. Kiến trúc

```text
com.inventory_concurrency
├── adapter/in/web          # REST Controller, Exception Handler
├── application
│   ├── dto                 # Request/Response DTO
│   ├── port                # Input/Output Ports
│   └── service             # Use Case Implementation
├── domain/model            # Inventory Entity và nghiệp vụ
└── infrastructure/persistence
                            # JPA Repository và Envers Adapter
```

Project áp dụng Hexagonal Architecture nhằm tách biệt nghiệp vụ, điều phối use case, giao tiếp HTTP và hạ tầng lưu trữ.

## 4. Cấu hình database

Tạo database MySQL:

```sql
CREATE DATABASE inventory_concurrency
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

Kiểm tra cấu hình trong `src/main/resources/application.yml`.

Mặc định project sử dụng:

- Host: `localhost`
- Port: `3306`
- Database: `inventory_concurrency`
- Username: `root`
- Password: đọc từ biến môi trường `DB_PASSWORD` (mặc định để trống)

Có thể cấu hình biến môi trường `DB_USERNAME` và `DB_PASSWORD` trước khi chạy ứng dụng.

Hibernate được cấu hình `ddl-auto: update` để tự cập nhật schema khi khởi động. Hibernate Envers sẽ quản lý các bảng audit tương ứng.

## 5. Cách chạy project

Yêu cầu: JDK 17 và MySQL đang hoạt động.

Tại thư mục gốc project, chạy:

```powershell
.\mvnw.cmd clean test
```

Khởi động ứng dụng:

```powershell
.\mvnw.cmd spring-boot:run
```

Ứng dụng mặc định chạy tại `http://localhost:8080`.

## 6. API

### Nhập kho

`POST /inventories/{productId}/stock-in`

Request:

```json
{
  "quantity": 5
}
```

Sử dụng Pessimistic Locking để khóa bản ghi tồn kho trong transaction.

### Cập nhật tồn kho

`PUT /inventories/{productId}`

Request:

```json
{
  "productName": "Keyboard Pro",
  "quantity": 20,
  "version": 0
}
```

Giá trị `version` phải khớp với phiên bản hiện tại của bản ghi. Hãy sử dụng version trả về từ API hoặc dữ liệu mới nhất trong database. Nếu có xung đột cập nhật, API có thể trả về `409 Conflict`.

### Xem lịch sử tồn kho

`GET /inventories/{productId}/history`

Trả về danh sách revision được Hibernate Envers ghi nhận, bao gồm thời điểm thay đổi, tên sản phẩm, số lượng và loại revision.

## 7. Các khái niệm quan trọng

- **Pessimistic Locking:** khóa bản ghi để hạn chế các transaction khác cập nhật đồng thời.
- **Optimistic Locking:** dùng `@Version` để phát hiện cập nhật dựa trên dữ liệu cũ.
- **Hibernate Envers:** lưu và truy vấn lịch sử phiên bản entity.
- **Hexagonal Architecture:** tách domain, application, adapter và infrastructure để tăng khả năng bảo trì và kiểm thử.

## 8. Kiểm thử

Các class test chính:

- `InventoryTest`: kiểm thử quy tắc nghiệp vụ của Inventory.
- `InventoryServiceTest`: kiểm thử logic service bằng Mockito.
- `InventoryControllerTest`: kiểm thử API bằng MockMvc.
- `InventoryConcurrencyApplicationTests`: kiểm tra khả năng khởi tạo Spring ApplicationContext.

Lưu ý: Unit test sử dụng mock không thay thế integration test với MySQL. Cần kiểm thử thêm với database thật để xác nhận hành vi locking và audit trong môi trường chạy thực tế.
