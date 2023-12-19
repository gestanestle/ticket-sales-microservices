package com.krimo.event_command.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "event_outbox")
public class EventOutbox {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false, length = 4096)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime timestamp;
    public EventOutbox(String topic, String payload, LocalDateTime timestamp) {
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static EventOutbox publish(String topic, String payload) {
        return new EventOutbox(topic, payload, LocalDateTime.now());
    }

}
