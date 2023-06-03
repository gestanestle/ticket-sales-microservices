package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.SectionAttendeeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @InjectMocks
    @Autowired
    private RedisServiceImpl redisService;

    String eventCode = "LBJ8DEJIKR";
    String cacheKey;
    String serializedMap;
    SectionAttendeeMap sectionAttendeeMap = new SectionAttendeeMap();

    @BeforeEach
    void setUp() {

        cacheKey = "event:" + eventCode;

        HashMap<Section, Integer> maxCapacity = new HashMap<>();
        maxCapacity.put(Section.VIP, 30);

        HashMap<Section, Integer> attendeeCount = new HashMap<>();
        maxCapacity.put(Section.VIP, 10);

        sectionAttendeeMap.setMaxCapacity(maxCapacity);
        sectionAttendeeMap.setAttendeeCount(attendeeCount);

        try {
            serializedMap = objectMapper.writeValueAsString(sectionAttendeeMap);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redisService = new RedisServiceImpl(objectMapper, redisTemplate);
    }

    @Test
    void getSectionAttendeeMap() {
        when(redisTemplate.opsForValue().get(cacheKey)).thenReturn(serializedMap);

        SectionAttendeeMap result = redisService.getSectionAttendeeMap(eventCode);

        // Assert
        verify(redisTemplate.opsForValue(), times(1)).get(cacheKey);
        assertEquals(30, result.getMaxCapacity().get(Section.VIP));
        assertEquals(10, result.getAttendeeCount().get(Section.VIP));
    }

    @Test
    void storeSectionAttendeeMap() {
        redisService.storeSectionAttendeeMap(eventCode, sectionAttendeeMap);
        verify(redisTemplate.opsForValue(), times(1)).set(eventCode, serializedMap);
    }
}