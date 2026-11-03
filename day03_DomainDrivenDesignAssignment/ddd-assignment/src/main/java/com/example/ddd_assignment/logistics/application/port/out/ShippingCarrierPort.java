package com.example.ddd_assignment.logistics.application.port.out;

import java.math.BigDecimal;

public interface ShippingCarrierPort {

    BigDecimal calculateShippingFee(ShippingRequest request);
}