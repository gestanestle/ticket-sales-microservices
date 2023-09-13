package com.krimo.event_command.controller;

import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.service.EventCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/events")
@RequiredArgsConstructor
public class EventCommandController {

    private final EventCommandService eventCommandService;

    @PostMapping
    public ResponseEntity<Long> createEvent(@RequestBody EventDTO eventDTO) {
        Long eventId = eventCommandService.createEvent(eventDTO);
        return new ResponseEntity<>(eventId, HttpStatus.CREATED);
    }

    @PutMapping(path = "{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable("eventId") Long eventId,
                                              @RequestBody EventDTO eventDTO) {
        eventCommandService.updateEvent(eventId, eventDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(path = "{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("eventId") Long eventId) {
        eventCommandService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
