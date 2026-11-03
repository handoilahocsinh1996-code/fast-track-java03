package com.example.clean_architecture.adapters.inbound.web;

import com.example.clean_architecture.adapters.inbound.web.request.OrderItemRequest;
import com.example.clean_architecture.adapters.inbound.web.request.PlaceOrderRequest;
import com.example.clean_architecture.adapters.inbound.web.response.PlaceOrderResponse;
import com.example.clean_architecture.application.order.dto.PlaceOrderCommand;
import com.example.clean_architecture.application.order.dto.PlaceOrderResult;
import com.example.clean_architecture.application.order.port.in.PlaceOrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<PlaceOrderResponse> placeOrder(
            @RequestBody PlaceOrderRequest request
    ) {

        PlaceOrderCommand command = new PlaceOrderCommand(
                request.customerId(),
                request.items()
                        .stream()
                        .map(this::toCommandItem)
                        .toList()
        );

        PlaceOrderResult result =
                placeOrderUseCase.placeOrder(command);

        PlaceOrderResponse response = new PlaceOrderResponse(
                result.orderId(),
                result.total()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    private PlaceOrderCommand.Item toCommandItem(
            OrderItemRequest item
    ) {
        return new PlaceOrderCommand.Item(
                item.productId(),
                item.quantity(),
                item.unitPrice()
        );
    }
}
