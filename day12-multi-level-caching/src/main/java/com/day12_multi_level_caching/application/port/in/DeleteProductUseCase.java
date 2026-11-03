package com.day12_multi_level_caching.application.port.in;

public interface DeleteProductUseCase {

    boolean deleteProductById(Long id);
}