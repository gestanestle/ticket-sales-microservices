package com.krimo.ticket.data;

import com.krimo.ticket.dto.TicketDTO;

import java.time.LocalDateTime;

public class TicketTest {

    final static Long EVENT_ID = 1L;
    final static String TICKET_CODE = "TICKETCODE";
    final static Long PURCHASED_BY = 1L;

    public static Ticket ticketInit() {
        return Ticket.create(EVENT_ID, Section.VIP, TICKET_CODE, PURCHASED_BY);
    }

    public static TicketDTO ticketDTOInit() {
        return TicketDTO.builder()
                .id(1L)
                .eventId(EVENT_ID)
                .section(Section.VIP)
                .ticketCode(TICKET_CODE)
                .purchasedBy(PURCHASED_BY)
                .purchasedAt(LocalDateTime.now())
                .build();
    }
}
