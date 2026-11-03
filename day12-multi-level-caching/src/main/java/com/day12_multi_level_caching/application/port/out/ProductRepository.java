package com.day12_multi_level_caching.application.port.out;

import com.day12_multi_level_caching.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    Product save(Product product);

    boolean deleteById(Long id);
}
