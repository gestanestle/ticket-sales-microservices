package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {

    @JsonProperty("ticket_id")
    private Long id;
    @JsonProperty("event_id")
    private Long eventId;
    private Section section;
    @JsonProperty("ticket_code")
    private String ticketCode;
    @JsonProperty("purchased_by")
    private Long purchasedBy;
    @JsonProperty("purchased_at")
    private LocalDateTime purchasedAt;
}
