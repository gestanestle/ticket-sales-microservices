package com.krimo.event_command.controller;

import com.krimo.event_command.dto.EventDTO;
import com.krimo.event_command.dto.ResponseObject;
import com.krimo.event_command.service.EventCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v3/events")
@RequiredArgsConstructor
public class EventCommandController {

    private final EventCommandService eventCommandService;

    @PostMapping
    public ResponseEntity<ResponseObject> createEvent(@RequestBody EventDTO eventDTO) {
        String message = "Event successfully created.";
        Long eventId = eventCommandService.createEvent(eventDTO);
        Object data = Map.of("eventId", eventId);
        return new ResponseEntity<>(ResponseObject.of(message, HttpStatus.CREATED, data), HttpStatus.CREATED);
    }

    @PutMapping(path = "{eventId}")
    public ResponseEntity<ResponseObject> updateEvent(@PathVariable("eventId") Long eventId,
                                              @RequestBody EventDTO eventDTO) {
        String message = "Event successfully updated.";
        eventCommandService.updateEvent(eventId, eventDTO);
        return new ResponseEntity<>(ResponseObject.of(message, HttpStatus.OK, null), HttpStatus.OK);
    }
    
    @DeleteMapping(path = "{eventId}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("eventId") Long eventId) {
        String message = "Event successfully deleted.";
        eventCommandService.deleteEvent(eventId);
        return new ResponseEntity<>(ResponseObject.of(message, HttpStatus.OK, null), HttpStatus.OK);
    }

}
