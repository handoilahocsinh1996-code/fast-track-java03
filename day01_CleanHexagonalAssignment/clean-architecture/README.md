Clean Architecture
Run Project

1. Create MySQL database

Make sure MySQL is running, then create:

CREATE DATABASE clean_architecture;

2. Configure database

Check src/main/resources/application.properties and update the MySQL username/password if needed.

3. Start application

On Windows:

.\mvnw.cmd clean spring-boot:run

Application runs at:

http://localhost:8080

Hibernate will automatically create the required tables.

Test API
Place Order

POST

http://localhost:8080/orders

Create a request.json file:

{
"customerId": "customer-001",
"items": [
{
"productId": "product-001",
"quantity": 2,
"unitPrice": 100000
}
]
}

Run:

curl.exe -X POST "http://localhost:8080/orders" `  -H "Content-Type: application/json"`
--data-binary "@request.json"

Expected response:

{
"orderId": "generated-order-id",
"total": 200000
}

Verify Database
USE clean_architecture;

SELECT _ FROM orders;
SELECT _ FROM order_items;
