package com.day12_multi_level_caching.application.port.in;

import com.day12_multi_level_caching.domain.model.Product;

import java.util.Optional;

public interface GetProductUseCase {

    Optional<Product> getProductById(Long id);
}