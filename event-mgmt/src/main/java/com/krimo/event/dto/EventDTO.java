package com.krimo.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.event.data.Section;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@Builder
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

    public EventDTO() {
    }

    public EventDTO(String eventCode, String venue, LocalDateTime dateTime, String title, String details, HashMap<Section, Integer> maxCapacity, String organizer) {
        this.eventCode = eventCode;
        this.venue = venue;
        this.dateTime = dateTime;
        this.title = title;
        this.details = details;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
    }

    public EventDTO(String venue, LocalDateTime dateTime, String title, String details, HashMap<Section, Integer> maxCapacity, String organizer) {
        this.venue = venue;
        this.dateTime = dateTime;
        this.title = title;
        this.details = details;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
    }
}
