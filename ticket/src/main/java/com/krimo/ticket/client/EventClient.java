package com.krimo.ticket.client;

import com.krimo.ticket.dto.TicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("event")
public interface EventClient {

    @PutMapping("api/v1/event/{eventCode}/attendees")
    ResponseEntity<Object> addAttendees(@PathVariable("eventCode") String eventCode,
                                       @RequestBody TicketDTO ticketDTO);
}
