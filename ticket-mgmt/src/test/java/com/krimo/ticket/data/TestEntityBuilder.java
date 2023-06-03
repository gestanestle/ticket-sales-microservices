package com.krimo.ticket.data;

import com.krimo.ticket.dto.TicketDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestEntityBuilder {
    private static final String EVENT_CODE = "303EAB8XXF";
    private static final String TICKET_CODE = "LBJ8DEJIKR";
    private static final String EMAIL = "email@gmail.com";

    public TestEntityBuilder() {
    }

    public Ticket ticket() {
        return Ticket.builder()
                .ticketCode(TICKET_CODE)
                .eventCode(EVENT_CODE)
                .section(Section.VIP)
                .purchaseDateTime(LocalDateTime.now())
                .purchaserEmail(EMAIL)
                .build();
    }

    public TicketDTO ticketDTO() {
        return TicketDTO.builder()
                .eventCode(EVENT_CODE)
                .section(Section.VIP)
                .purchaserEmail(EMAIL)
                .build();
    }
}