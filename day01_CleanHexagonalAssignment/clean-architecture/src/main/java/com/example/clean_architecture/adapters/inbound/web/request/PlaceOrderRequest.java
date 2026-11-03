package com.example.clean_architecture.adapters.inbound.web.request;

import java.util.List;

public record PlaceOrderRequest(
        String customerId,
        List<OrderItemRequest> items
) {
}
