package com.krimo.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.event.data.Section;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class EventDTO {

    @JsonProperty("event_code")
    private String eventCode;
    private String name;
    private String venue;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    private String details;
    @JsonProperty("max_capacity")
    private HashMap<Section, Integer> maxCapacity;
    @JsonProperty("attendee_count")
    private HashMap<Section, Integer> attendeeCount;
    private String organizer;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    // Request DTO
    public EventDTO(String name, String venue,
                    LocalDateTime dateTime, String details,
                    HashMap<Section, Integer> maxCapacity, String organizer) {
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
        this.details = details;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
    }

    // Response DTO
    public EventDTO(String eventCode, String name, String venue,
                    LocalDateTime dateTime, String details,
                    HashMap<Section, Integer> maxCapacity,
                    HashMap<Section, Integer> attendeeCount,
                    String organizer, LocalDateTime createdAt) {
        this.eventCode = eventCode;
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
        this.details = details;
        this.maxCapacity = maxCapacity;
        this.attendeeCount = attendeeCount;
        this.organizer = organizer;
        this.createdAt = createdAt;
    }
}
