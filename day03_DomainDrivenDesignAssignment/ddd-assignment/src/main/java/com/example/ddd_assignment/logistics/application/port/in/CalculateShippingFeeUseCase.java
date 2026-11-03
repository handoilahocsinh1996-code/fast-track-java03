package com.example.ddd_assignment.logistics.application.port.in;

import com.example.ddd_assignment.logistics.application.port.out.ShippingRequest;

import java.math.BigDecimal;

public interface CalculateShippingFeeUseCase {

    BigDecimal calculate(String carrier, ShippingRequest request);
}