package com.krimo.ticket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Event{

    @Id
    private Long eventId;
    private Boolean isActive;
    @OneToMany(cascade = CascadeType.ALL)
    Set<Ticket> tickets;
}
