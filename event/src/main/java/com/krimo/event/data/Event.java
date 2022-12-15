package com.krimo.event.data;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {

    @Id
    @SequenceGenerator(name = "eo_seq", sequenceName = "eo_seq", allocationSize = 1)
    @GeneratedValue(generator = "eo_seq", strategy = GenerationType.AUTO)
    private Long id;
    private String eventCode;
    private String venue;
    private LocalDateTime dateTime;
    private String title;
    private String details;
    private HashMap<Section, Integer> maxCapacity;
    private HashMap<Section, Integer> registeredAttendees;
    private String organizer;

    public Event(String eventCode,
                 String venue, LocalDateTime dateTime,
                 String title,
                 String details,
                 HashMap<Section, Integer> maxCapacity,
                 HashMap<Section, Integer> registeredAttendees,
                 String organizer) {
        this.eventCode = eventCode;
        this.venue = venue;
        this.dateTime = dateTime;
        this.title = title;
        this.details = details;
        this.maxCapacity = maxCapacity;
        this.registeredAttendees = registeredAttendees;
        this.organizer = organizer;
    }
}
