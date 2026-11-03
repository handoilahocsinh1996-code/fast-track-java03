package com.day12_multi_level_caching.application.port.in;

import com.day12_multi_level_caching.domain.model.Product;

public interface CreateProductUseCase {

    Product createProduct(String name, java.math.BigDecimal price);
}