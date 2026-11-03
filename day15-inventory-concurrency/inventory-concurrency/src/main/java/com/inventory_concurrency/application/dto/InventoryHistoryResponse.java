package com.inventory_concurrency.application.dto;

import java.time.Instant;

public record InventoryHistoryResponse(
                long revision,
                Instant changedAt,
                String productName,
                int quantity,
                String revisionType) {
}