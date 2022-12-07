package com.krimo.ticket.repository;

import com.krimo.ticket.data.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Event, String> {
    @Query("{eventCode:?0}")
    Optional<Event> findByEventCode(String eventCode);
}
