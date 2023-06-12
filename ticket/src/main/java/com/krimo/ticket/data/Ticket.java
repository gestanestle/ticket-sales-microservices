package com.krimo.ticket.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private Long id;
    @JsonProperty("event_code")
    private String eventCode;
    @JsonProperty("ticket_code")
    private String ticketCode;
    private Section section;
    @JsonProperty("purchaser_email")
    private String purchaserEmail;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public Ticket(String eventCode, String ticketCode, Section section, String purchaserEmail, LocalDateTime createdAt) {
        this.eventCode = eventCode;
        this.ticketCode = ticketCode;
        this.section = section;
        this.purchaserEmail = purchaserEmail;
        this.createdAt = createdAt;
    }

    public Ticket(String eventCode, Section section, String purchaserEmail) {
        this.eventCode = eventCode;
        this.section = section;
        this.purchaserEmail = purchaserEmail;
    }
}
