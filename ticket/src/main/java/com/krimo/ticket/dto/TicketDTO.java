package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TicketDTO {

    @JsonProperty("event_code")
    private String eventCode;
    @JsonProperty("ticket_code")
    private String ticketCode;
    private Section section;
    @JsonProperty("purchaser_email")
    private String purchaserEmail;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;


    // Response object
    public TicketDTO(String eventCode, String ticketCode, Section section, String purchaserEmail, LocalDateTime createdAt) {
        this.eventCode = eventCode;
        this.ticketCode = ticketCode;
        this.section = section;
        this.purchaserEmail = purchaserEmail;
        this.createdAt = createdAt;
    }

    // Request object
    public TicketDTO(String eventCode, Section section, String purchaserEmail) {
        this.eventCode = eventCode;
        this.section = section;
        this.purchaserEmail = purchaserEmail;
    }
}
