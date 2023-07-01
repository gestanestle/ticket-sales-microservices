package com.krimo.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    private Long id;
    private String name;
    private String description;
    private String venue;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("created_At")
    private LocalDateTime createdAt;
    @JsonProperty("is_canceled")
    private Boolean isCanceled;

}


