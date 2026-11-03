package com.example.ddd_assignment.logistics.infrastructure.adapter;

import com.example.ddd_assignment.logistics.application.port.out.ShippingCarrierPort;
import com.example.ddd_assignment.logistics.application.port.out.ShippingRequest;

import java.math.BigDecimal;

public class GhnShippingAdapter implements ShippingCarrierPort {

    @Override
    public BigDecimal calculateShippingFee(ShippingRequest request) {
        return request.weight().multiply(new BigDecimal("15000"));
    }
}