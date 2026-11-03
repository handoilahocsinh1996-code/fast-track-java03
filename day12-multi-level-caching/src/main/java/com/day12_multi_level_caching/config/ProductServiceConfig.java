package com.day12_multi_level_caching.config;

import com.day12_multi_level_caching.application.port.out.CacheInvalidationPublisher;
import com.day12_multi_level_caching.application.port.out.ProductCache;
import com.day12_multi_level_caching.application.port.out.ProductRepository;
import com.day12_multi_level_caching.application.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceConfig {

    @Bean
    public ProductService productService(
            ProductRepository productRepository,
            ProductCache productCache,
            CacheInvalidationPublisher cacheInvalidationPublisher) {
        return new ProductService(
                productRepository,
                productCache,
                cacheInvalidationPublisher);
    }
}