package com.krimo.ticket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseId;
    private String ticketCode;
    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PurchaseStatus status;
    private Long customerId;
    private LocalDateTime createdAt;

    public Purchase(String ticketCode, Ticket ticket, PurchaseStatus status, Long customerId, LocalDateTime createdAt) {
        this.ticketCode = ticketCode;
        this.ticket = ticket;
        this.status = status;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public static Purchase create(String ticketCode, Ticket ticket, PurchaseStatus status, Long customerId) {
        return new Purchase(ticketCode, ticket, status, customerId, LocalDateTime.now());
    }
}
