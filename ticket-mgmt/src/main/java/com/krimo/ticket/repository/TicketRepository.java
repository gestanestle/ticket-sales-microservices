package com.krimo.ticket.repository;

import com.krimo.ticket.data.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t.purchaserEmail from Ticket t WHERE t.eventCode=?1")
    List<String> getEmails(String eventCode);
}
