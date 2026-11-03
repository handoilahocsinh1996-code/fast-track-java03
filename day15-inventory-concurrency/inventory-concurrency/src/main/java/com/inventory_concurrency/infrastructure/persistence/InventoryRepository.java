package com.inventory_concurrency.infrastructure.persistence;

import com.inventory_concurrency.domain.model.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.productId = :productId
            """)
    Optional<Inventory> findByIdForUpdate(
            @Param("productId") Long productId);
}