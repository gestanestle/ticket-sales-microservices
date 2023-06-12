package com.krimo.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyClass
public class EventCapacityKey implements Serializable {

    @PrimaryKeyColumn(name = "seat", type = PrimaryKeyType.PARTITIONED)
    private String section;

    @PrimaryKeyColumn(name = "event_code", type = PrimaryKeyType.CLUSTERED)
    private String eventCode;

}
