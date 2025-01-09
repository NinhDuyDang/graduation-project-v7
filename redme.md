Bước 1 cài đặt môi trường 
# Book Store

Đây là một dự án quản lý cửa hàng sách, sử dụng **Spring Boot** cùng các thư viện hỗ trợ.

## Yêu cầu hệ thống

- **Java 8** hoặc mới hơn.
- **Maven** 3.6+.
- **MySQL** để làm cơ sở dữ liệu.
- 
Bước 2 cài đặt cơ sở dữ liệu
## spring.datasource.url=jdbc:mysql://localhost:3306/<TEN_DATABASE>
## spring.datasource.username=<USERNAME>
## spring.datasource.password=<PASSWORD>

Bước 3: Cài đặt các thư viện: Chạy lệnh sau để tải tất cả dependencies:
## mvn clean install

Bước 4: Chạy ứng dụng
## mvn spring-boot:run
http://localhost:8080
