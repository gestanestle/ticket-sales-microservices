package com.krimo.ticket.controller;

import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v2/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping()
    public ResponseEntity<Object> purchaseTicket(@RequestBody Ticket ticket) {
        ticketService.createTicket(ticket);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping(path = "{eventCode}")
    public ResponseEntity<List<String>> getAllPurchaserEmails(@PathVariable ("eventCode") String eventCode) {
        return new ResponseEntity<>(ticketService.getEmailsList(eventCode), HttpStatus.OK);
    }


}
