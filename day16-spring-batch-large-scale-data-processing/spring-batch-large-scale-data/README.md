# Spring Batch Large-Scale Data Migration

## 1. Giới thiệu

Ứng dụng sử dụng **Spring Batch** để di chuyển dữ liệu quy mô lớn từ PostgreSQL sang MySQL, hỗ trợ đọc dữ liệu theo trang, chuyển đổi cấu trúc Entity, ghi dữ liệu hàng loạt và theo dõi tiến trình thông qua Batch Metadata.

## 2. Công nghệ sử dụng

- Java 17
- Spring Boot 3.4.5
- Spring Batch
- Spring Data JPA
- PostgreSQL — Database nguồn
- MySQL — Database đích
- Maven

## 3. Kiến trúc xử lý

```text
PostgreSQL
    |
    v
JpaPagingItemReader
    |
    v
UserItemProcessor
    |
    v
JdbcBatchItemWriter
    |
    v
MySQL

Spring Batch Metadata:
Theo dõi trạng thái Job và Step
```

### Các thành phần chính

- `SourceUserEntity`: Entity ánh xạ dữ liệu nguồn.
- `TargetUserEntity`: Entity đại diện dữ liệu đích.
- `JpaPagingItemReader`: Đọc dữ liệu PostgreSQL theo từng trang.
- `UserItemProcessor`: Kiểm tra, chuẩn hóa và chuyển đổi Entity.
- `JdbcBatchItemWriter`: Ghi dữ liệu theo lô vào MySQL.
- `BatchConfig`: Cấu hình Job và Step.
- `MigrationController`: API khởi chạy migration.
- `MigrationScheduler`: Lên lịch chạy migration.

## 4. Cấu hình database

Tạo hai database:

- PostgreSQL: `legacy_db`
- MySQL: `migration_db`

Cấu hình thông tin kết nối trong `src/main/resources/application.yml` hoặc thông qua các biến môi trường:

```powershell
$env:SOURCE_DB_USERNAME="postgres"
$env:SOURCE_DB_PASSWORD="your_source_password"
$env:TARGET_DB_USERNAME="root"
$env:TARGET_DB_PASSWORD="your_target_password"
```

Tạo bảng `users` tương ứng trong cả hai database trước khi chạy migration.

## 5. Chạy ứng dụng

Yêu cầu PostgreSQL và MySQL đã được cài đặt, khởi động và có thể kết nối.

Mở terminal tại thư mục gốc project:

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

## 6. Khởi chạy migration

Gửi HTTP POST đến API:

```http
POST http://localhost:8080/api/migrations
```

API trả về mã thực thi Job nếu yêu cầu khởi chạy thành công.

## 7. Theo dõi và khôi phục

Spring Batch Metadata lưu thông tin thực thi Job và Step, hỗ trợ theo dõi trạng thái xử lý.

Reader bật `saveState` để lưu trạng thái đọc. Khả năng restart cần được kiểm thử với cùng Job instance và identifying parameters; việc khôi phục sau lỗi không có nghĩa là hai database được cập nhật trong cùng một distributed transaction.

## 8. Kết quả mong đợi

- Đọc dữ liệu theo trang từ PostgreSQL.
- Chuyển đổi dữ liệu bằng `ItemProcessor`.
- Ghi hàng loạt dữ liệu vào MySQL.
- Theo dõi trạng thái xử lý qua Spring Batch Metadata.
- Hỗ trợ tiếp tục Job sau sự cố khi cấu hình restart phù hợp.
