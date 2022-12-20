package com.krimo.ticket.dto;

import com.krimo.ticket.data.Event;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventList {

    private List<Event> eventList;
}
