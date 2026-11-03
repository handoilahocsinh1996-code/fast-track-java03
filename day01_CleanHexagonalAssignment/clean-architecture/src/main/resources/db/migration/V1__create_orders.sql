CREATE TABLE orders (
    id VARCHAR(36) NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    total DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);