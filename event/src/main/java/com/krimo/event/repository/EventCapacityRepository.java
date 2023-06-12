package com.krimo.event.repository;

import com.krimo.event.data.EventCapacity;
import com.krimo.event.data.EventCapacityKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCapacityRepository extends CassandraRepository<EventCapacity, EventCapacityKey> {

    @Query("SELECT * FROM event_capacity WHERE event_code = ?0")
    Iterable<EventCapacity> findByEventCode(String eventCode);

    @Query("DELETE FROM event_capacity WHERE event_code = ?0")
    void deleteByEventCode(String eventCode);
}
