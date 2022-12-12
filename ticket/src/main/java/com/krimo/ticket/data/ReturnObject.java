package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnObject {

    private String errorMsg;
    private Ticket ticket;

    public ReturnObject(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ReturnObject(Ticket ticket) {
        this.ticket = ticket;
    }
}
