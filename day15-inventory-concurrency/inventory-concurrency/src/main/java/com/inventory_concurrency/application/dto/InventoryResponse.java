package com.inventory_concurrency.application.dto;

public record InventoryResponse(
        Long productId,
        String productName,
        Integer quantity,
        Long version) {
}