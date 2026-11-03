package com.inventory_concurrency.application.service;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(Long productId) {
        super("Inventory not found for product ID: " + productId);
    }
}