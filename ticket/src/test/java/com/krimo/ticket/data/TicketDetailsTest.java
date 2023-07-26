package com.krimo.ticket.data;

import com.krimo.ticket.dto.TicketDetailsDTO;

public class TicketDetailsTest {

    final static Long EVENT_ID = 1L;
    final static Integer PRICE = 10000;
    final static Integer TOTAL_STOCK = 500;

    public static TicketDetails ticketDetailsVIP() {
        return TicketDetails.create(TicketDetailsPK.of(EVENT_ID, Section.VIP), PRICE, TOTAL_STOCK);
    }

    public static TicketDetails ticketDetailsUB() {
        return TicketDetails.create(TicketDetailsPK.of(EVENT_ID, Section.UPPER_BOX), PRICE, TOTAL_STOCK);
    }
    public static TicketDetailsDTO ticketDetailsDTOInit() {
        return TicketDetailsDTO.builder()
                .eventId(EVENT_ID).section(Section.VIP)
                .price(PRICE).totalStock(TOTAL_STOCK)
                .build();
    }

}
