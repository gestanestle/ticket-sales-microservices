package com.krimo.event.repository;

import com.krimo.event.data.Event;
import com.krimo.event.data.TicketDetails;
import com.krimo.event.data.TicketDetailsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails, TicketDetailsPK> {

    @Query("SELECT t FROM TicketDetails t WHERE t.pk.event = ?1")
    List<TicketDetails> getTicketDetailsByEvent(Event event);

}
