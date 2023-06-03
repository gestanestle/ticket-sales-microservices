package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.data.SectionAttendeeMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

public interface RedisService {
    SectionAttendeeMap getSectionAttendeeMap(String eventCode);
    void storeSectionAttendeeMap(String eventCode, SectionAttendeeMap sectionAttendeeMap);
}

@Service
@RequiredArgsConstructor
class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    private final ObjectMapper objectMapper;
    private static final String EVENT_CACHE_KEY_PREFIX = "event:";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public SectionAttendeeMap getSectionAttendeeMap(String eventCode) {
        String cacheKey = EVENT_CACHE_KEY_PREFIX + eventCode;

        SectionAttendeeMap sectionAttendeeMap = new SectionAttendeeMap();
        String val =  redisTemplate.opsForValue().get(cacheKey);
        if (val == null) { return null; }

        try {
            sectionAttendeeMap = objectMapper.readValue(val, SectionAttendeeMap.class);
        } catch (JsonProcessingException e) {
            logger.debug("Failed to deserialize from redis to HashMap. \n"  + e.getCause());
        }
        return sectionAttendeeMap;
    }

    @Override
    public void storeSectionAttendeeMap(String eventCode, SectionAttendeeMap sectionAttendeeMap) {
        String cacheKey = EVENT_CACHE_KEY_PREFIX + eventCode;
        String serializedMap = "";
        try {
            serializedMap = objectMapper.writeValueAsString(sectionAttendeeMap);
        } catch (JsonProcessingException e) {
            logger.debug("Failed to serialize hashmap to redis. \n" + e.getCause());
        }
        redisTemplate.opsForValue().set(cacheKey, serializedMap);
    }

}
