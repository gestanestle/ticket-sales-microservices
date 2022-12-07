package com.krimo.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {

    private String venue;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    private String title;
    private String details;
    @JsonProperty("max_capacity")
    private Integer maxCapacity;
    private String organizer;

}
