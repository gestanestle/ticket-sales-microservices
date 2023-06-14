package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyClass
public class EventCapacityKey implements Serializable {

    @PrimaryKeyColumn(name = "event_code", type = PrimaryKeyType.PARTITIONED)
    private String eventCode;
    @PrimaryKeyColumn(name = "seat", type = PrimaryKeyType.CLUSTERED)
    private String section;


}
