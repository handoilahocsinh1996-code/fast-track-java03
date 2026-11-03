package com.day12_multi_level_caching.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "Product name must not be blank") @Size(max = 150, message = "Product name must not exceed 150 characters") String name,

        @NotNull(message = "Product price is required") @DecimalMin(value = "0.00", message = "Product price must be greater than or equal to zero") BigDecimal price

) {
}