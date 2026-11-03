package com.day12_multi_level_caching.application.port.out;

public interface CacheInvalidationPublisher {

    void publishProductInvalidation(Long productId);
}
