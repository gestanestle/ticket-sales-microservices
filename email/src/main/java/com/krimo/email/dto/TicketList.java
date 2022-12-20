package com.krimo.email.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketList {

    private List<Ticket> ticketList;
}
