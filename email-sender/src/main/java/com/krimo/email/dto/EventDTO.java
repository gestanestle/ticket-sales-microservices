package com.krimo.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private String eventCode;
    private String venue;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    private String title;
    private String details;
    @JsonProperty("max_capacity")
    private HashMap<Section, Integer> maxCapacity;
    private String organizer;

}
