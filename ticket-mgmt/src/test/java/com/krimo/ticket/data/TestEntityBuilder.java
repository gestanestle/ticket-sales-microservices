package com.krimo.ticket.data;

import com.krimo.ticket.dto.TicketDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestEntityBuilder {

    private final static String EVENT_CODE = "303EAB8XXF";
    private final static String TICKET_CODE = "LBJ8DEJIKR";
    private final static String EMAIL = "email@gmail.com";

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
