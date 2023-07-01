package com.krimo.event.controller;

import com.krimo.event.dto.EventDTO;
import com.krimo.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Long> createEvent(@RequestBody EventDTO eventDTO) {
        Long eventId = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(eventId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping(path = "{eventId}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable("eventId") Long eventId) {
        return new ResponseEntity<>(eventService.getEvent(eventId), HttpStatus.OK);
    }

    @PutMapping(path = "{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable("eventId") Long eventId,
                                              @RequestBody EventDTO eventDTO) {
        eventService.updateEvent(eventId, eventDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
