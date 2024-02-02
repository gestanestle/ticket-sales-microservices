package com.krimo.ticket.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder @ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ticketId;
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;
    private String type;
    private Double price;
    private Integer qtyStock;
    private Integer qtySold;

    public Ticket(Event event, String type, Double price, Integer qtyStock, Integer qtySold) {
        this.event = event;
        this.type = type;
        this.price = price;
        this.qtyStock = qtyStock;
        this.qtySold = qtySold;
    }

    public static Ticket create(Event event, String type, Double price, Integer qtyStock) {
        return new Ticket(event, type, price, qtyStock, 0);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Ticket ticket)) return false;
        return Objects.equals(ticketId, ticket.ticketId) && Objects.equals(event, ticket.event) && Objects.equals(type, ticket.type) && Objects.equals(price, ticket.price) && Objects.equals(qtyStock, ticket.qtyStock) && Objects.equals(qtySold, ticket.qtySold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, event, type, price, qtyStock, qtySold);
    }
}
