package com.inventory_concurrency.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateInventoryRequest(

        @NotBlank(message = "Product name is required") String productName,

        @NotNull(message = "Quantity is required") @PositiveOrZero(message = "Quantity cannot be negative") Integer quantity,

        @NotNull(message = "Version is required") @PositiveOrZero(message = "Version must be zero or greater") Long version

) {
}