package com.krimo.ticket.controller;

import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.service.TicketPurchaseService;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v2/ticket")
@RequiredArgsConstructor
public class TicketMgmtController {

    private static final Logger logger = LoggerFactory.getLogger(TicketMgmtController.class);
    private final TicketPurchaseService ticketPurchaseService;
    private final TicketService ticketService;

    @PostMapping()
    public ResponseEntity<String> registerToEvent(@RequestBody TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketPurchaseService.purchaseTicket(ticketDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/emails/{eventCode}")
    public ResponseEntity<List<String>> emailsList(@PathVariable("eventCode") String eventCode) {
        logger.info("EVENT CODE REQUESTING EMAILS: " + eventCode);
        return new ResponseEntity<>(ticketService.emailsList(eventCode), HttpStatus.OK);
    }


}
