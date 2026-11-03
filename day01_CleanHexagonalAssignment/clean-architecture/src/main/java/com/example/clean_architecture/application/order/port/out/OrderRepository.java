package com.example.clean_architecture.application.order.port.out;

import com.example.clean_architecture.domain.order.entity.Order;

public interface OrderRepository {

    Order save(Order order);
}
