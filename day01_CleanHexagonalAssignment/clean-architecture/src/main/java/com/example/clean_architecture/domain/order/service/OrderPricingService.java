package com.example.clean_architecture.domain.order.service;

import com.example.clean_architecture.domain.order.entity.Order;

import java.math.BigDecimal;

public class OrderPricingService {

    public BigDecimal calculateTotal(Order order) {

        return order.getItems()
                .stream()
                .map(item -> item.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
