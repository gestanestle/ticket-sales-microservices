package com.krimo.event.data;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(name = "event", indexes = @Index(name = "idx_event_name", columnList = "event_name"))
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "event_name")
    private String name;
    private String description;
    private String venue;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_At")
    private LocalDateTime createdAt;
    @Column(name = "is_canceled")
    private Boolean isCanceled;

    public Event(String name, String description, String venue, LocalDateTime dateTime, String createdBy, LocalDateTime createdAt, Boolean isCanceled) {
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.dateTime = dateTime;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.isCanceled = isCanceled;
    }

    public static Event create(String name, String description, String venue,
                                    LocalDateTime dateTime, String createdBy) {
        return new Event(name, description, venue, dateTime, createdBy,
                        LocalDateTime.now(), false);
    }

}
