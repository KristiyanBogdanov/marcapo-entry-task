package com.example.springbootservice.auth;

import com.example.springbootservice.model.user.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheStorage {
    @Autowired
    private RedisTemplate<String, List<Permission>> listRedisTemplate;

    public void add(String userId, List<Permission> permissions) {
        listRedisTemplate.opsForValue().set(userId, permissions);
    }

    public boolean contains(String userId) {
        return Boolean.TRUE.equals(listRedisTemplate.hasKey(userId));
    }

    public List<Permission> get(String userId) {
        return listRedisTemplate.opsForValue().get(userId);
    }

    public boolean remove(String userId) {
        return Boolean.TRUE.equals(listRedisTemplate.delete(userId));
    }
}
