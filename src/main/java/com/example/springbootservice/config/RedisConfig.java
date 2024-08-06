package com.example.springbootservice.config;

import com.example.springbootservice.model.user.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, List<Permission>> listRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Permission>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}