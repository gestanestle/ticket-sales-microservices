package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krimo.ticket.data.Event;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping()
    public ResponseEntity<Ticket> buyTicket(@RequestBody TicketDTO ticketDTO) throws JsonProcessingException {
        Ticket ticket = ticketService.buyTicket(ticketDTO);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Event>> allEvents() {
        List<Event> events = ticketService.allEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<List<Ticket>> eventTickets(@PathVariable("eventCode") String eventCode) {
        List<Ticket> tickets = ticketService.eventTickets(eventCode);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
