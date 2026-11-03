package com.example.clean_architecture.adapters.inbound.web.request;

import java.math.BigDecimal;

public record OrderItemRequest(
                String productId,
                int quantity,
                BigDecimal unitPrice) {
}
