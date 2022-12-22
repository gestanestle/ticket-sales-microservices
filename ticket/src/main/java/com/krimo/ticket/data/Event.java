package com.krimo.ticket.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "events")
public class Event {
    @Id
    private String eventCode;
    private Collection<Ticket> tickets;

}
