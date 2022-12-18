package com.krimo.ticket.dto;

import com.krimo.ticket.data.Ticket;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketList {

    private List<Ticket> ticketList;
}
