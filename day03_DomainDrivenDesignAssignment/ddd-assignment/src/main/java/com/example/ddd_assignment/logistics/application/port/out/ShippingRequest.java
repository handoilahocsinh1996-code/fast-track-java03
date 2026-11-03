package com.example.ddd_assignment.logistics.application.port.out;

import java.math.BigDecimal;

public record ShippingRequest(
        String fromAddress,
        String toAddress,
        BigDecimal weight) {
}