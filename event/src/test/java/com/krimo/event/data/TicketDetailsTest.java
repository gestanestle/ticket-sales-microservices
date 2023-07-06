package com.krimo.event.data;

import com.krimo.event.dto.TicketDetailsDTO;

public class TicketDetailsTest {

    static Event EVENT = EventTest.eventInit();
    final static Long ID = 1L;
    final static Integer PRICE = 10000;
    final static Integer TOTAL_STOCK = 500;

    public static TicketDetails ticketDetailsVIP() {
        EVENT.setId(ID);
        return TicketDetails.create(new TicketDetailsPK(EVENT, Section.VIP), PRICE, TOTAL_STOCK);
    }

    public static TicketDetails ticketDetailsUB() {
        EVENT.setId(ID);
        return TicketDetails.create(new TicketDetailsPK(EVENT, Section.UPPER_BOX), PRICE, TOTAL_STOCK);
    }
    public static TicketDetailsDTO ticketDetailsDTOInit() {
        return TicketDetailsDTO.builder()
                .eventId(ID).section(Section.VIP)
                .price(PRICE).totalStock(TOTAL_STOCK).totalSold(null)
                .build();
    }

}
