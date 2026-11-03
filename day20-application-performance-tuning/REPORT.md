# BÁO CÁO PHÂN TÍCH VÀ TỐI ƯU HIỆU NĂNG ỨNG DỤNG

## 1. Tổng quan dự án

**Tên dự án:** Day 20 - Application Performance Analysis & Tuning

**Công nghệ sử dụng:**
- Java 17
- Spring Boot 3.4.5
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Database:** `performance_tuning_db`

**Dữ liệu mẫu:**
- 30 tác giả (Authors).
- Mỗi tác giả có 100 cuốn sách (Books).
- Tổng cộng 3.000 cuốn sách.

## 2. Phân tích các vấn đề hiệu năng

### 2.1. Vấn đề 1: N+1 Query

**Mô tả vấn đề:**

Khi lấy danh sách tác giả và các cuốn sách tương ứng, ứng dụng ban đầu sử dụng `findAll()` để lấy toàn bộ tác giả.

Sau đó, ứng dụng truy cập danh sách sách của từng tác giả thông qua quan hệ `LAZY`.

Do đó, Hibernate phải thực hiện thêm truy vấn SQL để lấy sách tương ứng với từng tác giả.

Đây chính là vấn đề N+1 Query.

**Code ban đầu:**

```java
List<Author> authors = authorRepository.findAll();

return authors.stream()
        .map(this::toResponse)
        .toList();
```

**Cách phát hiện:**

Bật SQL Logging trong `application.properties`:

```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE
```

Gọi API:

```http
GET /api/authors/with-books/n-plus-one
```

Với 30 tác giả, số lượng truy vấn dự kiến:

- 1 truy vấn lấy danh sách tác giả.
- 30 truy vấn lấy danh sách sách của từng tác giả.
- Tổng cộng khoảng 31 câu lệnh SELECT.

**Nguyên nhân:**

Ứng dụng sử dụng cơ chế Lazy Loading và truy cập collection của từng Entity.

Mỗi lần truy cập collection chưa được tải, Hibernate có thể phát sinh thêm một câu truy vấn SQL.

**Giải pháp:**

Sử dụng `@EntityGraph` để tải tác giả và danh sách sách liên quan cùng lúc.

Tại `AuthorRepository`:

```java
@EntityGraph(attributePaths = "books")
@Query("SELECT DISTINCT a FROM Author a")
List<Author> findAllWithBooks();
```

Tại `AuthorService`:

```java
List<Author> authors = authorRepository.findAllWithBooks();

return authors.stream()
        .map(this::toResponse)
        .toList();
```

**Kết quả mong đợi:**

Hibernate sử dụng truy vấn Fetch để lấy tác giả và sách liên quan, thay vì phát sinh thêm một truy vấn cho mỗi tác giả.

Điều này giúp giảm số lần truy cập Database và hạn chế các truy vấn SQL không cần thiết.

---

### 2.2. Vấn đề 2: Thiếu Database Index

**Mô tả vấn đề:**

Ứng dụng thường xuyên tìm kiếm sách theo năm xuất bản thông qua phương thức:

```java
List<Book> findByPublicationYear(int publicationYear);
```

Nếu cột `publication_year` không có Index phù hợp, MySQL có thể phải quét nhiều bản ghi trong bảng `book` để tìm dữ liệu thỏa mãn điều kiện.

Khi số lượng bản ghi tăng lên, việc này có thể ảnh hưởng đến hiệu năng truy vấn.

**Cách phát hiện:**

Bước 1: Kiểm tra các Index hiện có:

```sql
SHOW INDEX FROM book;
```

Bước 2: Phân tích kế hoạch thực thi truy vấn:

```sql
EXPLAIN
SELECT id, title, publication_year, author_id
FROM book
WHERE publication_year = 2020;
```

Quan sát các thông tin:
- `type`: Kiểu truy cập dữ liệu.
- `key`: Index được MySQL lựa chọn.
- `rows`: Số lượng bản ghi MySQL ước tính cần kiểm tra.

**Nguyên nhân:**

Cột `publication_year` chưa có Index phù hợp để hỗ trợ truy vấn tìm kiếm theo năm xuất bản.

Do đó, MySQL có thể phải thực hiện Full Table Scan.

**Giải pháp:**

Nếu chưa tồn tại Index tương đương, tạo Index cho cột `publication_year`:

```sql
CREATE INDEX idx_book_publication_year
ON book(publication_year);
```

Sau đó chạy lại câu lệnh `EXPLAIN` để kiểm tra kế hoạch thực thi.

**Kết quả mong đợi:**

Index giúp MySQL có thêm phương án truy cập dữ liệu hiệu quả khi tìm kiếm sách theo năm xuất bản.

Lưu ý: MySQL Optimizer có thể vẫn lựa chọn Full Table Scan nếu bảng có ít dữ liệu hoặc điều kiện truy vấn trả về phần lớn bản ghi. Việc tạo Index không đảm bảo MySQL luôn sử dụng Index trong mọi trường hợp.

---

## 3. Tổng hợp các giải pháp tối ưu

| Vấn đề | Nguyên nhân | Giải pháp |
|---|---|---|
| N+1 Query | Lazy Loading các Entity liên quan | Sử dụng `@EntityGraph` |
| Thiếu Index | Truy vấn trên cột chưa có Index phù hợp | Tạo Index cho `publication_year` |

## 4. Kiểm thử và xác minh

Các API được xây dựng để kiểm tra vấn đề N+1 Query:

**API chưa tối ưu:**

```http
GET http://localhost:8080/api/authors/with-books/n-plus-one
```

**API đã tối ưu:**

```http
GET http://localhost:8080/api/authors/with-books/optimized
```

**API tìm kiếm sách theo năm xuất bản:**

```http
GET http://localhost:8080/api/books?publicationYear=2020
```

Các công cụ được sử dụng để phân tích:
- Hibernate SQL Logging: Quan sát các câu truy vấn SQL.
- MySQL EXPLAIN: Phân tích kế hoạch thực thi truy vấn.
- SHOW INDEX: Kiểm tra các Index hiện có trong Database.

## 5. Phạm vi Benchmark

Trong phạm vi bài tập này, không thực hiện Benchmark Before/After.

Thay vào đó, tập trung vào việc nhận diện các vấn đề hiệu năng, phân tích nguyên nhân và áp dụng các giải pháp tối ưu phù hợp.

## 6. Kết luận

Thông qua bài tập, đã tìm hiểu và xử lý hai vấn đề hiệu năng phổ biến trong ứng dụng Spring Boot:

1. **N+1 Query:** Phát sinh nhiều truy vấn SQL không cần thiết khi sử dụng Lazy Loading.
2. **Thiếu Database Index:** Có thể khiến truy vấn tìm kiếm dữ liệu phải quét nhiều bản ghi.

Việc sử dụng `@EntityGraph` giúp tối ưu quá trình truy xuất các Entity có quan hệ với nhau.

Việc bổ sung Database Index phù hợp giúp cải thiện khả năng tìm kiếm dữ liệu khi quy mô Database tăng lên.

Qua đó, bài tập giúp hiểu rõ hơn về cách phân tích, phát hiện và tối ưu hiệu năng truy vấn Database trong ứng dụng Java Spring Boot.