package com.example.clean_architecture.application.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record PlaceOrderCommand(
        String customerId,
        List<Item> items
) {

    public record Item(
            String productId,
            int quantity,
            BigDecimal unitPrice
    ) {
    }
}
