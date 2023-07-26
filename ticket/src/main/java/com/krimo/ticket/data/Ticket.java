package com.krimo.ticket.data;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;
    @Column(name = "event_id")
    private Long eventId;
    @Enumerated(EnumType.STRING)
    private Section section;
    @Column(name = "ticket_code")
    private String ticketCode;
    @Column(name = "purchased_by")
    private Long purchasedBy;
    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;


    public Ticket(Long eventId, Section section, String ticketCode, Long purchasedBy, LocalDateTime purchasedAt) {
        this.eventId = eventId;
        this.section = section;
        this.ticketCode = ticketCode;
        this.purchasedBy = purchasedBy;
        this.purchasedAt = purchasedAt;
    }

    public static Ticket create(Long eventId, Section section, String ticketCode, Long purchasedBy) {
        return new Ticket(eventId, section, ticketCode, purchasedBy, LocalDateTime.now());
    }

}
