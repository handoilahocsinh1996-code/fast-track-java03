package com.inventory_concurrency.application.port.in;

import com.inventory_concurrency.application.dto.InventoryHistoryResponse;
import com.inventory_concurrency.application.dto.InventoryResponse;
import com.inventory_concurrency.application.dto.StockInRequest;
import com.inventory_concurrency.application.dto.UpdateInventoryRequest;

import java.util.List;

public interface InventoryUseCase {

    InventoryResponse stockIn(Long productId, StockInRequest request);

    InventoryResponse updateInventory(
            Long productId,
            UpdateInventoryRequest request);

    List<InventoryHistoryResponse> getInventoryHistory(Long productId);
}