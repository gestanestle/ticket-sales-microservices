package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table("event_capacity")
public class EventCapacity {

    @PrimaryKey
    private EventCapacityKey key;
    @Column("max_capacity")
    private int maxCapacity;
    @Column("attendee_count")
    private int attendeeCount;

}
