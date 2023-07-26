package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.data.Section;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDetailsDTO {

    @JsonProperty("event_id")
    private Long eventId;
    private Section section;
    private Integer price;
    @JsonProperty("total_stock")
    private Integer totalStock;
}
