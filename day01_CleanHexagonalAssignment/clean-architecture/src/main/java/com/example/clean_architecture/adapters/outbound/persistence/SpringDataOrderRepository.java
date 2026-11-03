package com.example.clean_architecture.adapters.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository
        extends JpaRepository<OrderJpaEntity, String> {
}
