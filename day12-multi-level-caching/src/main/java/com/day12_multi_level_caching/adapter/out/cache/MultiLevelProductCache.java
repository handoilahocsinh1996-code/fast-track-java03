package com.day12_multi_level_caching.adapter.out.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.day12_multi_level_caching.application.port.out.ProductCache;
import com.day12_multi_level_caching.domain.model.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MultiLevelProductCache implements ProductCache {

    private static final String CACHE_PREFIX = "product:";

    private final Cache<Long, Product> localCache;
    private final RedisTemplate<String, Product> redisTemplate;

    public MultiLevelProductCache(
            Cache<Long, Product> productLocalCache,
            RedisTemplate<String, Product> redisTemplate) {
        this.localCache = productLocalCache;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<Product> get(Long id) {
        // L1: Check local Caffeine cache first.
        Product localProduct = localCache.getIfPresent(id);

        if (localProduct != null) {
            return Optional.of(localProduct);
        }

        // L2: Check shared Redis cache.
        Product redisProduct = redisTemplate.opsForValue()
                .get(cacheKey(id));

        if (redisProduct != null) {
            // Promote the product from L2 to L1.
            localCache.put(id, redisProduct);
            return Optional.of(redisProduct);
        }

        return Optional.empty();
    }

    @Override
    public void put(Product product) {
        if (product == null || product.id() == null) {
            return;
        }

        localCache.put(product.id(), product);

        redisTemplate.opsForValue()
                .set(cacheKey(product.id()), product);
    }

    @Override
    public void evict(Long id) {
        localCache.invalidate(id);
        redisTemplate.delete(cacheKey(id));
    }

    private String cacheKey(Long id) {
        return CACHE_PREFIX + id;
    }
}