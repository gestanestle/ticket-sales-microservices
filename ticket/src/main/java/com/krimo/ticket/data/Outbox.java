package com.krimo.ticket.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "outbox")
public class Outbox {

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

    public Outbox() {
    }

    public Outbox(String topic, String payload, LocalDateTime timestamp) {
        this.topic = topic;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    public static Outbox publish(String topic, String payload) {
        return new Outbox(topic, payload, LocalDateTime.now());
    }


}
