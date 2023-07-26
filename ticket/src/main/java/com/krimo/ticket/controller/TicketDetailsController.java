package com.krimo.ticket.controller;

import com.krimo.ticket.dto.TicketDetailsDTO;
import com.krimo.ticket.service.TicketDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/event")
@RequiredArgsConstructor
@Slf4j
public class TicketDetailsController {

    private final TicketDetailsService ticketDetailsService;

    @PostMapping(path = "{eventId}/ticket-details")
    public ResponseEntity<Object> setTicketDetails(@PathVariable("eventId") Long eventId, @RequestBody TicketDetailsDTO dto) {
        ticketDetailsService.setTicketDetails(eventId, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "{eventId}/ticket-details")
    public ResponseEntity<List<TicketDetailsDTO>> getTicketDetailsByEvent(@PathVariable("eventId") Long eventId) {
        return new ResponseEntity<>(ticketDetailsService.getTicketDetailsByEvent(eventId), HttpStatus.OK);
    }



}
