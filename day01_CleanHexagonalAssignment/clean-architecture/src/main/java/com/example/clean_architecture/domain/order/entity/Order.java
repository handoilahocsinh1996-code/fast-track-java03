package com.example.clean_architecture.domain.order.entity;

import com.example.clean_architecture.domain.order.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private String id;
    private String customerId;
    private List<OrderItem> items;
    private BigDecimal total;

    public Order(
            String id,
            String customerId,
            List<OrderItem> items
    ) {

        if (customerId == null || customerId.isBlank()) {
            throw new InvalidOrderException(
                    "Customer ID is required"
            );
        }

        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException(
                    "Order must contain at least one item"
            );
        }

        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = BigDecimal.ZERO;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
