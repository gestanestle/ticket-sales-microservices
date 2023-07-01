package com.krimo.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.event.data.Section;
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
    @JsonProperty("remaining_stock")
    private Integer remainingStock;
}
