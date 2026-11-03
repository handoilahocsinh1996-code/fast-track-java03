package com.day12_multi_level_caching.adapter.out.cache;

import com.day12_multi_level_caching.application.port.out.CacheInvalidationPublisher;
import com.day12_multi_level_caching.config.RedisPubSubConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheInvalidationPublisher
        implements CacheInvalidationPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic cacheInvalidationTopic;

    public RedisCacheInvalidationPublisher(
            StringRedisTemplate redisTemplate,
            ChannelTopic cacheInvalidationTopic) {
        this.redisTemplate = redisTemplate;
        this.cacheInvalidationTopic = cacheInvalidationTopic;
    }

    @Override
    public void publishProductInvalidation(Long productId) {
        redisTemplate.convertAndSend(
                cacheInvalidationTopic.getTopic(),
                String.valueOf(productId));
    }
}