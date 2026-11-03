package com.day12_multi_level_caching.adapter.out.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.day12_multi_level_caching.domain.model.Product;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisCacheInvalidationSubscriber implements MessageListener {

    private final Cache<Long, Product> localCache;

    public RedisCacheInvalidationSubscriber(
            Cache<Long, Product> productLocalCache) {
        this.localCache = productLocalCache;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String productId = new String(
                message.getBody(),
                StandardCharsets.UTF_8);

        try {
            Long id = Long.valueOf(productId);
            localCache.invalidate(id);
        } catch (NumberFormatException exception) {
            // Ignore invalid product IDs.
        }
    }
}