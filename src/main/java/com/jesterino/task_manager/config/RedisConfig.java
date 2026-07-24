package com.jesterino.task_manager.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(
        name = "cache.redis.enabled",
        havingValue = "true"
)
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        return new RedisTemplate<>();
    }
}
