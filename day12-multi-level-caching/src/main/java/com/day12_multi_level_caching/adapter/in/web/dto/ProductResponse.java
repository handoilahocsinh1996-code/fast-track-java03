package com.day12_multi_level_caching.adapter.in.web.dto;

import com.day12_multi_level_caching.domain.model.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.price());
    }
}