package com.krimo.ticket.controller;

import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/v2/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping()
    public ResponseEntity<TicketDTO> purchaseTicket(@RequestBody TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.purchaseTicket(ticketDTO), HttpStatus.CREATED);

    }
    
    @GetMapping(path="{ticketCode}")
    public ResponseEntity<TicketDTO> showTicket(@PathVariable("ticketCode") String ticketCode) {
        return new ResponseEntity<>(ticketService.getTicket(ticketCode), HttpStatus.OK);
    }

    @GetMapping(path = "{eventCode}/emails")
    public ResponseEntity<Set<String>> getAllPurchaserEmails(@PathVariable("eventCode") String eventCode) {
        return new ResponseEntity<>(ticketService.getEmailsSet(eventCode), HttpStatus.OK);
    }


}
