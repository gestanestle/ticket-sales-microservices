package com.krimo.notification.repository;

import com.krimo.notification.message.BrokerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


public interface BrokerMessageRepository {
    boolean isKeyPresent(String key);
    void saveMessage(BrokerMessage message);
}

@RequiredArgsConstructor
@Service
class RedisBrokerMessageRepositoryImpl implements BrokerMessageRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isKeyPresent(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void saveMessage(BrokerMessage message) {
        redisTemplate.opsForValue().set(message.id(), message.payload());
        redisTemplate.expire(message.id(), 24, TimeUnit.HOURS);
    }
}

