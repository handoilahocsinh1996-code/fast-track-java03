package com.example.clean_architecture.adapters.outbound.persistence;

import com.example.clean_architecture.application.order.port.out.OrderRepository;
import com.example.clean_architecture.domain.order.entity.Order;
import com.example.clean_architecture.domain.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository repository;

    public JpaOrderRepositoryAdapter(
            SpringDataOrderRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {

        OrderJpaEntity orderEntity =
                new OrderJpaEntity(
                        order.getId(),
                        order.getCustomerId(),
                        order.getTotal()
                );

        for (OrderItem item : order.getItems()) {

            OrderItemJpaEntity itemEntity =
                    new OrderItemJpaEntity(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getUnitPrice()
                    );

            orderEntity.addItem(itemEntity);
        }

        repository.save(orderEntity);

        return order;
    }
}
