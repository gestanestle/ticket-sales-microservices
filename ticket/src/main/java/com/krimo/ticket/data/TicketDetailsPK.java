package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TicketDetailsPK implements Serializable {

    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "section")
    @Enumerated(EnumType.STRING)
    private Section section;

    public static TicketDetailsPK of(Long eventId, Section section) { return new TicketDetailsPK(eventId, section); }

}
