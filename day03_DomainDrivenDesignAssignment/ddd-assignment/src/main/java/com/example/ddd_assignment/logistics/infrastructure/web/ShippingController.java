package com.example.ddd_assignment.logistics.infrastructure.web;

import com.example.ddd_assignment.logistics.application.factory.ShippingCarrierFactory;
import com.example.ddd_assignment.logistics.application.port.in.CalculateShippingFeeUseCase;
import com.example.ddd_assignment.logistics.application.port.out.ShippingRequest;
import com.example.ddd_assignment.logistics.application.service.CalculateShippingFeeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final CalculateShippingFeeUseCase useCase;

    public ShippingController() {
        this.useCase = new CalculateShippingFeeService(
                new ShippingCarrierFactory());
    }

    @PostMapping("/fee")
    public BigDecimal calculateFee(
            @RequestParam String carrier,
            @RequestBody ShippingRequest request) {
        return useCase.calculate(carrier, request);
    }
}