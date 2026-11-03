package com.day12_multi_level_caching.config;

import com.day12_multi_level_caching.adapter.out.cache.RedisCacheInvalidationSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisPubSubConfig {

    public static final String CACHE_INVALIDATION_CHANNEL = "product-cache-invalidation";

    @Bean
    public ChannelTopic cacheInvalidationTopic() {
        return new ChannelTopic(CACHE_INVALIDATION_CHANNEL);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisCacheInvalidationSubscriber subscriber,
            ChannelTopic cacheInvalidationTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriber, cacheInvalidationTopic);

        return container;
    }
}