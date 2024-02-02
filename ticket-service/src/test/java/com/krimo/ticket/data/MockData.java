package com.krimo.ticket.data;

import com.krimo.ticket.dto.PurchaseRequest;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.models.Purchase;
import com.krimo.ticket.models.PurchaseStatus;
import com.krimo.ticket.models.Ticket;

import java.util.HashSet;

public class MockData {

    public static Event eventInit() {
        return new Event(1L, true, new HashSet<>());
    }

    public static Ticket ticketInit() {
        return Ticket.create(eventInit(), "RESERVATION", 1000.00, 500);
    }

    public static TicketDTO ticketDTOInit() {
        return new TicketDTO(1L, 1L, "RESERVATION", 1000.00, 500, 0);
    }

    public static TicketDTO ticketDTU1() {
        return new TicketDTO(1L, 1L, "GENERAL ADMISSION", null, null, 0);
    }

    public static TicketDTO ticketDTU2() {
        return new TicketDTO(1L, 1L, null, 2000.00, null, 0);
    }

    public static TicketDTO ticketDTU3() {
        return new TicketDTO(1L, 1L, null, null, 499, 0);
    }

    public static Purchase purchaseInit() {
        return Purchase.create("ABCDE12345", ticketInit(), PurchaseStatus.BOOKED, 1L);
    }

    public static PurchaseRequest purchaseReq() {
        return new PurchaseRequest(1L, 3, null, 1L);
    }
}
