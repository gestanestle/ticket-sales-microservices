package com.krimo.event_query.controller;

import com.krimo.event_query.data.Event;
import com.krimo.event_query.service.EventQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v3/events")
public class EventQueryController {

    private final EventQueryService service;

    @Autowired
    public EventQueryController(EventQueryService service) {
        this.service = service;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @GetMapping(path = "{id}/name")
    public ResponseEntity<String> getNameById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getName(id), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Event>> searchEvent(@RequestParam("q") String query,
                                                   @RequestParam("pageNo") int pageNo,
                                                   @RequestParam("pageSize") int pageSize) {
        return new ResponseEntity<>(service.search(pageNo, pageSize, query), HttpStatus.OK);
    }

}
