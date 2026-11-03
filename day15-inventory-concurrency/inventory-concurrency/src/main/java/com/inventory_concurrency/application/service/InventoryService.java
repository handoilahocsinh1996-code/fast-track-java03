package com.inventory_concurrency.application.service;

import com.inventory_concurrency.application.dto.InventoryHistoryResponse;
import com.inventory_concurrency.application.dto.InventoryResponse;
import com.inventory_concurrency.application.dto.StockInRequest;
import com.inventory_concurrency.application.dto.UpdateInventoryRequest;
import com.inventory_concurrency.application.port.in.InventoryUseCase;
import com.inventory_concurrency.application.port.out.InventoryHistoryPort;
import com.inventory_concurrency.application.port.out.InventoryRepositoryPort;
import com.inventory_concurrency.domain.model.Inventory;

import jakarta.persistence.OptimisticLockException;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService implements InventoryUseCase {

    private final InventoryRepositoryPort inventoryRepository;
    private final InventoryHistoryPort inventoryHistoryPort;

    public InventoryService(
            InventoryRepositoryPort inventoryRepository,
            InventoryHistoryPort inventoryHistoryPort) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryHistoryPort = inventoryHistoryPort;
    }

    @Override
    @Transactional
    public InventoryResponse stockIn(
            Long productId,
            StockInRequest request) {
        Inventory inventory = inventoryRepository
                .findByIdForUpdate(productId)
                .orElseThrow(
                        () -> new InventoryNotFoundException(productId));

        inventory.stockIn(request.quantity());

        Inventory savedInventory = inventoryRepository.save(inventory);

        return toResponse(savedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(
            Long productId,
            UpdateInventoryRequest request) {
        Inventory inventory = inventoryRepository
                .findById(productId)
                .orElseThrow(
                        () -> new InventoryNotFoundException(productId));

        if (!inventory.getVersion().equals(request.version())) {
            throw new InventoryVersionConflictException(
                    "Inventory has been modified. Please refresh and try again.");
        }

        inventory.updateProduct(
                request.productName(),
                request.quantity());

        try {
            Inventory savedInventory = inventoryRepository.saveAndFlush(inventory);

            return toResponse(savedInventory);
        } catch (OptimisticLockingFailureException
                | OptimisticLockException exception) {
            throw new InventoryVersionConflictException(
                    "Inventory has been modified. Please refresh and try again.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getInventoryHistory(
            Long productId) {
        if (inventoryRepository.findById(productId).isEmpty()) {
            throw new InventoryNotFoundException(productId);
        }

        return inventoryHistoryPort.findHistoryByProductId(productId);
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getProductId(),
                inventory.getProductName(),
                inventory.getQuantity(),
                inventory.getVersion());
    }
}