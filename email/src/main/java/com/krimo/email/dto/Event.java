package com.krimo.email.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Long id;
    private String eventCode;
    private String venue;
    private LocalDateTime dateTime;
    private String title;
    private String details;
    private HashMap<Section, Integer> maxCapacity;
    private HashMap<Section, Integer> registeredAttendees;
    private String organizer;

}
