package com.krimo.event.controller;

import com.krimo.event.dto.EventDTO;
import com.krimo.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping()
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        String eventCode = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(eventCode, HttpStatus.CREATED);
    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<EventDTO> readEvent(@PathVariable("eventCode") String eventCode) {
        return new ResponseEntity<>(eventService.readEvent(eventCode), HttpStatus.OK);

    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<EventDTO>> readAllEvents() {
        return new ResponseEntity<>(eventService.readAllEvents(), HttpStatus.OK);
    }

    @PutMapping(path = "{eventCode}")
    public ResponseEntity<Object> updateEvent(@PathVariable("eventCode") String eventCode,
                                              @RequestBody EventDTO eventDTO) {
        eventService.updateEvent(eventCode, eventDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{eventCode}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("eventCode") String eventCode) {
        eventService.deleteEvent(eventCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
