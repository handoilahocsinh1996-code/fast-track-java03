package com.day12_multi_level_caching.domain.model;

import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        BigDecimal price) {
    public Product {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name must not be blank");
        }

        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException(
                    "Product price must be greater than or equal to zero");
        }
    }
}
