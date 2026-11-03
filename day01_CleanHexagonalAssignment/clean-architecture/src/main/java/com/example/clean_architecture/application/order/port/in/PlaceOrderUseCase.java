package com.example.clean_architecture.application.order.port.in;

import com.example.clean_architecture.application.order.dto.PlaceOrderCommand;
import com.example.clean_architecture.application.order.dto.PlaceOrderResult;

public interface PlaceOrderUseCase {

    PlaceOrderResult placeOrder(PlaceOrderCommand command);
}
