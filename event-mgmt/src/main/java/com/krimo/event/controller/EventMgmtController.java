package com.krimo.event.controller;

import com.krimo.event.data.Event;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.service.EventMgmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/event")
@RequiredArgsConstructor
public class EventMgmtController {

    private final EventMgmtService eventMgmtService;

    @PostMapping()
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(eventMgmtService.createEvent(eventDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<Event> readEvent(@PathVariable("eventCode") String eventCode) {
        return new ResponseEntity<>(eventMgmtService.readEvent(eventCode), HttpStatus.OK);

    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Event>> readAllEvents() {
        return new ResponseEntity<>(eventMgmtService.readAllEvents(), HttpStatus.OK);
    }

    @PutMapping(path = "{eventCode}")
    public ResponseEntity<Object> updateEvent(@PathVariable("eventCode") String eventCode,
                                              @RequestBody EventDTO eventDTO) {
        eventMgmtService.updateEvent(eventCode, eventDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{eventCode}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("eventCode") String eventCode) {
        eventMgmtService.deleteEvent(eventCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
