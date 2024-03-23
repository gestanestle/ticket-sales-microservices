package com.krimo.ticket.repository;

import com.krimo.ticket.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.event.eventId = ?1")
    List<Ticket> getByEvent(Long id);
}
