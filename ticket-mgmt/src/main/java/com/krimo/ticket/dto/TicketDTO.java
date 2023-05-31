package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TicketDTO {

    @JsonProperty("event_code")
    private String eventCode;
    private Section section;
    @JsonProperty("purchaser_email")
    private String purchaserEmail;

}