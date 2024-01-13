package com.krimo.event_command.data;

import com.krimo.event_command.data.types.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

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
    private String banner;
    private String description;
    private String venue;
    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;
    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;
    private String organizer;
    @ElementCollection
    private Set<String> tags;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "created_At")
    private LocalDateTime createdAt;

    public Event(String name, String banner, String description, String venue, LocalDateTime startDateTime, LocalDateTime endDateTime, String organizer, Set<String> tags, Status status, LocalDateTime createdAt) {
        this.name = name;
        this.banner = banner;
        this.description = description;
        this.venue = venue;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.organizer = organizer;
        this.tags = tags;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Event create(String name, String banner, String description, String venue, LocalDateTime startDateTime, LocalDateTime endDateTime, String organizer, Set<String> tags) {
        return new Event(name, banner, description, venue, startDateTime, endDateTime, organizer, tags, Status.ACTIVE, LocalDateTime.now());
    }
}
