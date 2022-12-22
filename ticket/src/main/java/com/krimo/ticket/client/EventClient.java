package com.krimo.ticket.client;

import com.krimo.ticket.data.Section;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("event")
public interface EventClient {

    @PutMapping("api/v1/event/{eventCode}/attendees/{section}")
    ResponseEntity<Object> addAttendees(@PathVariable("eventCode") String eventCode,
                                        @PathVariable("section") Section section);
}
