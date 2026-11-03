package com.example.ddd_assignment.logistics.application.factory;

import com.example.ddd_assignment.logistics.application.port.out.ShippingCarrierPort;
import com.example.ddd_assignment.logistics.infrastructure.adapter.GhnShippingAdapter;
import com.example.ddd_assignment.logistics.infrastructure.adapter.GhtkShippingAdapter;

public class ShippingCarrierFactory {

    public ShippingCarrierPort getCarrier(String carrier) {
        return switch (carrier.toUpperCase()) {
            case "GHN" -> new GhnShippingAdapter();
            case "GHTK" -> new GhtkShippingAdapter();
            default -> throw new IllegalArgumentException(
                    "Unsupported shipping carrier: " + carrier);
        };
    }
}