# JVM Performance Analysis: String Concatenation

## 1. Mục tiêu

Phân tích hiệu năng của API `/process`, xác định nguyên nhân gây lãng phí bộ nhớ và CPU, đồng thời đề xuất giải pháp tối ưu JVM và code Java.

## 2. Code ban đầu

Đoạn code được cung cấp trong bài tập:

```java
@GetMapping("/process")
public String processData() {
    long startTime = System.currentTimeMillis();

    String result = "";

    for (int i = 0; i < 150_000; i++) {
        result += " " + i;
    }

    long endTime = System.currentTimeMillis();

    return "Processing finished in "
            + (endTime - startTime)
            + "ms. Result length: "
            + result.length();
}
```

## 3. Vấn đề phát hiện

### 3.1. High GC Churn

Biểu thức `result += " " + i` được thực hiện 150.000 lần. Vì `String` là immutable, việc nối chuỗi lặp lại có thể tạo nhiều đối tượng trung gian, làm tăng lượng bộ nhớ cấp phát và áp lực lên Garbage Collector.

### 3.2. High CPU Usage

Nội dung chuỗi cũ liên tục được sao chép khi nối thêm dữ liệu. Tổng chi phí xử lý có thể tăng gần O(n²), khiến request mất nhiều thời gian.

### 3.3. High Latency

Thời gian xử lý lâu làm tăng latency của endpoint `/process`, ảnh hưởng đến khả năng đáp ứng request.

### 3.4. Có phải Memory Leak không?

Đây không phải Memory Leak điển hình. Các chuỗi trung gian thường không còn được tham chiếu và có thể được GC thu hồi. Tuy nhiên, chuỗi kết quả cuối cùng vẫn chiếm bộ nhớ trong thời gian xử lý.

## 4. Nguyên nhân gốc rễ

Nguyên nhân chính là sử dụng phép nối `String` trong vòng lặp lớn.

Các vấn đề bao gồm:

- Tạo nhiều đối tượng chuỗi trung gian.
- Sao chép nội dung chuỗi lặp lại.
- Tăng lượng bộ nhớ cấp phát và công việc của GC.
- Tăng thời gian xử lý API.

## 5. Code sau khi tối ưu

Sử dụng `StringBuilder` để hạn chế việc tạo chuỗi trung gian:

```java
@GetMapping("/process")
public String processData() {
    long startTime = System.nanoTime();

    StringBuilder result = new StringBuilder();

    for (int i = 0; i < 150_000; i++) {
        result.append(' ').append(i);
    }

    long endTime = System.nanoTime();

    return "Processing finished in "
            + ((endTime - startTime) / 1_000_000.0)
            + "ms. Result length: "
            + result.length();
}
```

### Lợi ích

- Giảm số lượng đối tượng chuỗi trung gian.
- Giảm lượng bộ nhớ cấp phát và áp lực GC.
- Cải thiện thời gian xử lý trong nhiều trường hợp.
- Sử dụng `System.nanoTime()` để đo thời gian thực thi.

## 7. JVM Tuning

Các cờ JVM có thể dùng để theo dõi và chuẩn hóa môi trường:

```text
-Xms256m
-Xmx256m
-XX:+UseG1GC
-Xlog:gc*:file=gc.log
```

Ý nghĩa:

- `-Xms256m`: Đặt Heap ban đầu là 256 MB.
- `-Xmx256m`: Giới hạn Heap tối đa là 256 MB.
- `-XX:+UseG1GC`: Sử dụng G1 Garbage Collector.
- `-Xlog:gc*:file=gc.log`: Ghi log hoạt động GC ra file.

Các tùy chọn này hỗ trợ đo lường và phân tích, nhưng không thay thế việc tối ưu code.

## 8. Kết luận

Vấn đề chính của đoạn code là nối chuỗi lặp lại, gây chi phí sao chép dữ liệu và cấp phát bộ nhớ cao. Sử dụng `StringBuilder` giúp giảm số lượng đối tượng trung gian và cải thiện hiệu năng.

Cần sử dụng VisualVM, GC Log và phép đo thời gian để xác nhận mức cải thiện thực tế trước khi kết luận.
