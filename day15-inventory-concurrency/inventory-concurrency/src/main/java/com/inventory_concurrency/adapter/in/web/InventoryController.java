package com.inventory_concurrency.adapter.in.web;

import com.inventory_concurrency.application.dto.InventoryHistoryResponse;
import com.inventory_concurrency.application.dto.InventoryResponse;
import com.inventory_concurrency.application.dto.StockInRequest;
import com.inventory_concurrency.application.dto.UpdateInventoryRequest;
import com.inventory_concurrency.application.port.in.InventoryUseCase;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryUseCase inventoryUseCase;

    public InventoryController(InventoryUseCase inventoryUseCase) {
        this.inventoryUseCase = inventoryUseCase;
    }

    @PostMapping("/{productId}/stock-in")
    public ResponseEntity<InventoryResponse> stockIn(
            @PathVariable Long productId,
            @Valid @RequestBody StockInRequest request) {
        InventoryResponse response = inventoryUseCase.stockIn(productId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryRequest request) {
        InventoryResponse response = inventoryUseCase.updateInventory(productId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}/history")
    public ResponseEntity<List<InventoryHistoryResponse>> getInventoryHistory(
            @PathVariable Long productId) {
        List<InventoryHistoryResponse> history = inventoryUseCase.getInventoryHistory(productId);

        return ResponseEntity.ok(history);
    }
}