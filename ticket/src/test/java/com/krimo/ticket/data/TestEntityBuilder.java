package com.krimo.ticket.data;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TestEntityBuilder {

    private final static String EVENT_CODE = "d21d68f9-667c-42c0-8a0a-7bc01db1a45a";
    private final static String EMAIL = "email@gmail.com";

    public Ticket ticket() {
        return Ticket.builder()
                .ticketCode(UUID.randomUUID().toString())
                .eventCode(EVENT_CODE)
                .section(Section.VIP)
                .purchaseDateTime(LocalDateTime.now())
                .customerEmail(EMAIL)
                .build();
    }

    public CustomerDTO customerDTO() {
        return CustomerDTO.builder()
                .eventCode(EVENT_CODE)
                .section(Section.VIP)
                .customerEmail(EMAIL)
                .build();
    }

}
