package com.krimo.event.data;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event", indexes = @Index(name = "idx_event_code", columnList = "event_code"))
public class Event {

    @Id
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq", allocationSize = 1)
    @GeneratedValue(generator = "event_seq", strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "event_code")
    private String eventCode;
    @Column(name = "name")
    private String name;
    @Column(name = "venue")
    private String venue;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String details;
    private String organizer;
    private LocalDateTime createdAt;

    public Event(String eventCode,
                 String name,
                 String venue,
                 LocalDateTime dateTime,
                 String details,
                 String organizer,
                 LocalDateTime createdAt) {
        this.eventCode = eventCode;
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
        this.details = details;
        this.organizer = organizer;
        this.createdAt = createdAt;
    }
}
