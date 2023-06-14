package com.krimo.ticket.repository;

import com.krimo.ticket.data.EventCapacity;
import com.krimo.ticket.data.EventCapacityKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCapacityRepository extends CassandraRepository<EventCapacity, EventCapacityKey> {

    @Query("SELECT * FROM event_capacity WHERE event_code = ?0 AND seat = ?1")
    EventCapacity getEventCapacity(String eventCode, String seat);

    @Query("UPDATE event_capacity " +
            "SET attendee_count = ?0 " +
            "WHERE event_code = ?1 AND seat = ?2")
    void incrementAttendeeCount(int count, String eventCode, String seat);
}
