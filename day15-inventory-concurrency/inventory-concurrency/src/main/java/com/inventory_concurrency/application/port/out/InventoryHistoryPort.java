package com.inventory_concurrency.application.port.out;

import com.inventory_concurrency.application.dto.InventoryHistoryResponse;

import java.util.List;

public interface InventoryHistoryPort {

    List<InventoryHistoryResponse> findHistoryByProductId(Long productId);
}