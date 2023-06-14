package com.krimo.ticket.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
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

}
