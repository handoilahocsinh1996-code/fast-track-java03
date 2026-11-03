package com.example.clean_architecture.config;

import com.example.clean_architecture.application.order.port.in.PlaceOrderUseCase;
import com.example.clean_architecture.application.order.port.out.OrderRepository;
import com.example.clean_architecture.application.order.service.PlaceOrderService;
import com.example.clean_architecture.domain.order.service.OrderPricingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderPricingService orderPricingService() {
        return new OrderPricingService();
    }

    @Bean
    public PlaceOrderUseCase placeOrderUseCase(
            OrderRepository orderRepository,
            OrderPricingService pricingService
    ) {
        return new PlaceOrderService(
                orderRepository,
                pricingService
        );
    }
}
