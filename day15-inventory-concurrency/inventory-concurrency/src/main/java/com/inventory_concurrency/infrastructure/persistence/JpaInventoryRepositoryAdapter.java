package com.inventory_concurrency.infrastructure.persistence;

import com.inventory_concurrency.application.port.out.InventoryRepositoryPort;
import com.inventory_concurrency.domain.model.Inventory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaInventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final InventoryRepository inventoryRepository;

    public JpaInventoryRepositoryAdapter(
            InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Optional<Inventory> findById(Long productId) {
        return inventoryRepository.findById(productId);
    }

    @Override
    public Optional<Inventory> findByIdForUpdate(Long productId) {
        return inventoryRepository.findByIdForUpdate(productId);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory saveAndFlush(Inventory inventory) {
        return inventoryRepository.saveAndFlush(inventory);
    }
}