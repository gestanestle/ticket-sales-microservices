package com.krimo.ticket.repository;

import com.krimo.ticket.data.TicketDetails;
import com.krimo.ticket.data.TicketDetailsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails, TicketDetailsPK> {

    @Query("SELECT t FROM TicketDetails t WHERE t.pk.eventId = ?1")
    List<TicketDetails> getTicketDetailsByEvent(Long eventId);

}
