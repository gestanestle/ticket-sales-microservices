package com.krimo.ticket.controller;

import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v2/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Long> purchaseTicket(@RequestBody TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.purchaseTicket(ticketDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "{ticketId}")
    public ResponseEntity<TicketDTO> viewTicket(@PathVariable("ticketId") Long ticketId) {
        return new ResponseEntity<>(ticketService.viewTicket(ticketId), HttpStatus.OK);
    }

    @DeleteMapping(path = "{ticketId}")
    public ResponseEntity<Object> cancelPurchase(@PathVariable("ticketId") Long ticketId) {
        ticketService.cancelPurchase(ticketId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
