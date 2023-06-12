package com.krimo.event.service;

import com.krimo.event.data.EventCapacity;
import com.krimo.event.repository.EventCapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface EventCapacityService {

    void setEventCapacity(EventCapacity eventCapacity);
    Iterable<EventCapacity> getEventCapacity(String eventCode);
    void deleteEvent(String eventCode);
}

@RequiredArgsConstructor
@Service
class EventCapacityServiceImpl implements EventCapacityService {

    private final EventCapacityRepository eventCapacityRepository;

    @Override
    public void setEventCapacity(EventCapacity eventCapacity) {
        eventCapacityRepository.save(eventCapacity);
    }

    @Override
    public Iterable<EventCapacity> getEventCapacity(String eventCode) {
        return eventCapacityRepository.findByEventCode(eventCode);
    }

    @Override
    public void deleteEvent(String eventCode) {
        eventCapacityRepository.deleteByEventCode(eventCode);
    }
}
