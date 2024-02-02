package com.krimo.ticket.controller;

import com.krimo.ticket.dto.ResponseBody;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v3")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(path = "event/{eventId}/tickets")
    ResponseEntity<ResponseBody> createTicket(@PathVariable("eventId") Long eventId, @RequestBody TicketDTO dto) {
        Long id = ticketService.createTicket(eventId, dto);
        return new ResponseEntity<>(ResponseBody.of(
                "Ticket successfully defined.",
                HttpStatus.CREATED,
                id
        ), HttpStatus.CREATED);
    }

    @GetMapping(path = "event/{eventId}/tickets")
    ResponseEntity<ResponseBody> getEventTickets(@PathVariable("eventId") Long id) {
        return new ResponseEntity<>(ResponseBody.of(
                "Event tickets successfully retrieved.",
                HttpStatus.OK,
                ticketService.getTicketsByEvent(id)
        ), HttpStatus.OK);
    }

    @GetMapping(path = "tickets/{ticketId}")
    ResponseEntity<ResponseBody> getTicket(@PathVariable("ticketId") Long id) {
        return new ResponseEntity<>(ResponseBody.of(
                "Ticket successfully retrieved.",
                HttpStatus.OK,
                ticketService.getTicket(id)
        ), HttpStatus.OK);
    }

    @PutMapping(path = "tickets/{ticketId}")
    ResponseEntity<ResponseBody> updateTicket(@PathVariable("ticketId") Long id, TicketDTO dto) {
        ticketService.updateTicket(id, dto);
        return new ResponseEntity<>(ResponseBody.of(
                "Ticket successfully updated.",
                HttpStatus.OK,
                null
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "tickets/{ticketId}")
    ResponseEntity<ResponseBody> deleteTicket(@PathVariable("ticketId") Long id) {
        ticketService.deleteTicket(id);
        return new ResponseEntity<>(ResponseBody.of(
                "Ticket successfully deleted.",
                HttpStatus.OK,
                null
        ), HttpStatus.OK);
    }
}
