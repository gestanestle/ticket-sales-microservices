package com.krimo.event_command.repository;

import com.krimo.event_command.data.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOutboxRepository extends JpaRepository<EventOutbox, Long> {
}
