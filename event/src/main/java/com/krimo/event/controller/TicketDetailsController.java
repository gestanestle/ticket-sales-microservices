package com.krimo.event.controller;

import com.krimo.event.data.Section;
import com.krimo.event.dto.TicketDetailsDTO;
import com.krimo.event.service.TicketDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/events")
@RequiredArgsConstructor
public class TicketDetailsController {

    private final TicketDetailsService ticketDetailsService;

    @PostMapping(path = "{eventId}/ticket-details")
    public ResponseEntity<Object> setTicketDetails(@PathVariable("eventId") Long eventId,
                                                   @RequestBody TicketDetailsDTO ticketDetailsDTO) {
        ticketDetailsService.setTicketDetails(eventId, ticketDetailsDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "{eventId}/ticket-details")
    public ResponseEntity<List<TicketDetailsDTO>> getTicketDetailsByEvent(@PathVariable("eventId") Long eventId) {
        return new ResponseEntity<>(ticketDetailsService.getTicketDetailsByEvent(eventId), HttpStatus.OK);
    }

    @PutMapping(path = "{eventId}/ticket-details/{section}")
    public ResponseEntity<Object> updateTicketDetails(@PathVariable("eventId") Long eventId,
                                                      @PathVariable("section") String section,
                                                      @RequestBody TicketDetailsDTO ticketDetailsDTO) {
        ticketDetailsService.updateTicketDetails(eventId, section, ticketDetailsDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
