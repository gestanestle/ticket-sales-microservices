package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krimo.ticket.dto.EventList;
import com.krimo.ticket.dto.ReturnObject;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.dto.TicketList;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping()
    public ResponseEntity<Object> buyTicket(@RequestBody TicketDTO ticketDTO) throws JsonProcessingException {
        ReturnObject returnObject = ticketService.buyTicket(ticketDTO);

        if (returnObject.getTicket() == null) {
            return new ResponseEntity<>(returnObject.getErrorMsg(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(returnObject.getTicket(), HttpStatus.OK);

    }

    @GetMapping(path = "/all")
    public ResponseEntity<EventList> allEvents() {
        return new ResponseEntity<>(ticketService.allEvents(), HttpStatus.OK);
    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<TicketList> eventTickets(@PathVariable("eventCode") String eventCode) {
        return new ResponseEntity<>(ticketService.eventTickets(eventCode), HttpStatus.OK);
    }

}
