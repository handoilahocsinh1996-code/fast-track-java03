package com.example.ddd_assignment.logistics.application.service;

import com.example.ddd_assignment.logistics.application.factory.ShippingCarrierFactory;
import com.example.ddd_assignment.logistics.application.port.in.CalculateShippingFeeUseCase;
import com.example.ddd_assignment.logistics.application.port.out.ShippingCarrierPort;
import com.example.ddd_assignment.logistics.application.port.out.ShippingRequest;

import java.math.BigDecimal;

public class CalculateShippingFeeService implements CalculateShippingFeeUseCase {

    private final ShippingCarrierFactory factory;

    public CalculateShippingFeeService(ShippingCarrierFactory factory) {
        this.factory = factory;
    }

    @Override
    public BigDecimal calculate(String carrier, ShippingRequest request) {
        ShippingCarrierPort shippingCarrier = factory.getCarrier(carrier);

        return shippingCarrier.calculateShippingFee(request);
    }
}