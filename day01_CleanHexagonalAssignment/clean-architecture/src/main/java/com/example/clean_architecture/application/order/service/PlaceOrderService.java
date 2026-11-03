package com.example.clean_architecture.application.order.service;

import com.example.clean_architecture.application.order.dto.PlaceOrderCommand;
import com.example.clean_architecture.application.order.dto.PlaceOrderResult;
import com.example.clean_architecture.application.order.port.in.PlaceOrderUseCase;
import com.example.clean_architecture.application.order.port.out.OrderRepository;
import com.example.clean_architecture.domain.order.entity.Order;
import com.example.clean_architecture.domain.order.entity.OrderItem;
import com.example.clean_architecture.domain.order.service.OrderPricingService;

import java.util.List;
import java.util.UUID;

public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPricingService pricingService;

    public PlaceOrderService(
            OrderRepository orderRepository,
            OrderPricingService pricingService
    ) {
        this.orderRepository = orderRepository;
        this.pricingService = pricingService;
    }

    @Override
    public PlaceOrderResult placeOrder(PlaceOrderCommand command) {

        List<OrderItem> items = command.items()
                .stream()
                .map(item -> new OrderItem(
                        item.productId(),
                        item.quantity(),
                        item.unitPrice()
                ))
                .toList();

        Order order = new Order(
                UUID.randomUUID().toString(),
                command.customerId(),
                items
        );

        var total = pricingService.calculateTotal(order);

        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        return new PlaceOrderResult(
                savedOrder.getId(),
                savedOrder.getTotal()
        );
    }
}
