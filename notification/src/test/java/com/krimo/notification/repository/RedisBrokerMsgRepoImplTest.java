package com.krimo.notification.repository;

import com.krimo.notification.message.BrokerMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisBrokerMsgRepoImplTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations valueOperations;
    @InjectMocks
    @Autowired
    private RedisBrokerMessageRepositoryImpl redisBrokerMessageRepository;

    @BeforeEach
    void setUp() {
        redisBrokerMessageRepository = new RedisBrokerMessageRepositoryImpl(redisTemplate);
    }

    @Test
    void isKeyPresent() {
        String key = "IDEMPOTENT";
        when(redisTemplate.hasKey(anyString())).thenReturn(Boolean.TRUE);
        assertTrue(redisBrokerMessageRepository.isKeyPresent(key));
    }

    @Test
    void saveMessage() {
        BrokerMessage message = new BrokerMessage("id", "payload");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString());

        redisBrokerMessageRepository.saveMessage(message);
        verify(redisTemplate.opsForValue(), times(1)).set(message.id(), message.payload());
    }
}
