package com.example.clean_architecture.application.order.dto;

import java.math.BigDecimal;

public record PlaceOrderResult(
        String orderId,
        BigDecimal total
) {
}
