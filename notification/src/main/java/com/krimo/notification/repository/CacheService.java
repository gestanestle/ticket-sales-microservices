package com.krimo.notification.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    boolean isKeyPresent(String key);
    void saveKeyValue(String key, String value);
    Map<String, String> getHashFromKey(String key);
    void saveHash(String key, String field, String value);
    void deleteHash(String key, String field);

}

@Service
class RedisServiceImpl implements CacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOps;
    private HashOperations<String, String, String> hashOps;

    @Autowired
    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
    }
    @Override
    public boolean isKeyPresent(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void saveKeyValue(String key, String value) {
        valueOps.set(key, value);
        redisTemplate.expire(key, 86400, TimeUnit.DAYS);
    }

    @Override
    public Map<String, String> getHashFromKey(String key) {
        return hashOps.entries(key);
    }

    @Override
    public void saveHash(String key, String field, String value) {
        hashOps.put(key, field, value);
    }

    @Override
    public void deleteHash(String key, String field) {
        hashOps.delete(key, field);
    }
}




