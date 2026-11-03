package com.day12_multi_level_caching.application.port.out;

import com.day12_multi_level_caching.domain.model.Product;

import java.util.Optional;

public interface ProductCache {

    Optional<Product> get(Long id);

    void put(Product product);

    void evict(Long id);
}
