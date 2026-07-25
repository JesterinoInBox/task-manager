package com.jesterino.task_manager.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@ConditionalOnProperty(
        name = "cache.redis.enabled",
        havingValue = "true"
)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        RedisSerializer<Object> jsonSerializer =
                RedisSerializer.json();

        template.setKeySerializer(
                RedisSerializer.string()
        );

        template.setHashKeySerializer(
                RedisSerializer.string()
        );

        template.setValueSerializer(
                jsonSerializer
        );

        template.setHashValueSerializer(
                jsonSerializer
        );

        template.afterPropertiesSet();

        return template;
    }
}