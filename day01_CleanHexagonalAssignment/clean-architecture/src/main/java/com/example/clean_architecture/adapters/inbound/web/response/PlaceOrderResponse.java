package com.example.clean_architecture.adapters.inbound.web.response;

import java.math.BigDecimal;

public record PlaceOrderResponse(
        String orderId,
        BigDecimal total
) {
}
