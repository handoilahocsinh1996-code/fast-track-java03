package com.example.clean_architecture.adapters.outbound.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal total;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    protected OrderJpaEntity() {
    }

    public OrderJpaEntity(
            String id,
            String customerId,
            BigDecimal total
    ) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<OrderItemJpaEntity> getItems() {
        return items;
    }

    public void addItem(OrderItemJpaEntity item) {
        items.add(item);
        item.setOrder(this);
    }
}
