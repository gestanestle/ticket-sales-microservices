package com.krimo.event.controller;

import com.krimo.event.data.Event;
import com.krimo.event.data.Section;
import com.krimo.event.dto.EventDTO;
import com.krimo.event.dto.TicketDTO;
import com.krimo.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/v1/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping()
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        String eventCode = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(eventCode, HttpStatus.CREATED);
    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<Event> readEvent(@PathVariable("eventCode") String eventCode) {
        Event event = eventService.readEvent(eventCode);
        return new ResponseEntity<>(event, HttpStatus.OK);

    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Event>> readAllEvents() {
        List<Event> events = eventService.readAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping(path = "{eventCode}")
    public ResponseEntity<String> updateEvent(@PathVariable("eventCode") String eventCode,
                                              @RequestBody EventDTO eventDTO) {
        eventService.updateEvent(eventCode, eventDTO);
        return new ResponseEntity<>(eventCode, HttpStatus.OK);
    }

    @DeleteMapping(path = "{eventCode}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventCode") String eventCode) {
        eventService.deleteEvent(eventCode);
        return new ResponseEntity<>(eventCode, HttpStatus.OK);
    }


    @PutMapping(path = "{eventCode}/attendees")
    public ResponseEntity<Collection<Section>> addAttendees(@PathVariable("eventCode") String eventCode,
                                                            @RequestBody TicketDTO ticketDTO) {
        Collection<Section> fullSections = eventService.addAttendee(eventCode, ticketDTO.getSection());
        return new ResponseEntity<>(fullSections, HttpStatus.OK);
    }

}
