package com.inventory_concurrency.application.service;

public class InventoryVersionConflictException extends RuntimeException {

    public InventoryVersionConflictException(String message) {
        super(message);
    }
}