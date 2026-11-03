package com.example.clean_architecture.domain.order.entity;

import com.example.clean_architecture.domain.order.exception.InvalidOrderException;

import java.math.BigDecimal;

public class OrderItem {

    private String productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem(
            String productId,
            int quantity,
            BigDecimal unitPrice
    ) {

        if (productId == null || productId.isBlank()) {
            throw new InvalidOrderException(
                    "Product ID is required"
            );
        }

        if (quantity <= 0) {
            throw new InvalidOrderException(
                    "Quantity must be greater than 0"
            );
        }

        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderException(
                    "Unit price must be greater than or equal to 0"
            );
        }

        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
