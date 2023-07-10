package com.krimo.ticket.repository;

import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.eventId = ?1 AND t.section = ?2")
    int getSold(Long eventId, Section section);

}