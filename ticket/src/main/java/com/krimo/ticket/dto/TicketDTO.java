package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TicketDTO {

    @JsonProperty("event_code")
    private String eventCode;
    private Section section;

}
