package com.inventory_concurrency.application.port.out;

import com.inventory_concurrency.domain.model.Inventory;

import java.util.Optional;

public interface InventoryRepositoryPort {

    Optional<Inventory> findById(Long productId);

    Optional<Inventory> findByIdForUpdate(Long productId);

    Inventory save(Inventory inventory);

    Inventory saveAndFlush(Inventory inventory);
}