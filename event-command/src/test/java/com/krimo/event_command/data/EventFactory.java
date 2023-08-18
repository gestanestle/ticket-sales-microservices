package com.krimo.event_command.data;

import com.krimo.event_command.dto.EventDTO;

import java.time.LocalDateTime;
public class EventFactory {

    private final static String NAME = "The Eras Tour";
    private final static String DESCRIPTION = "The Taylor Swift world tour concert after Midnights.";
    private final static String VENUE = "Mall of Asia";
    private final static LocalDateTime DATE_TIME = LocalDateTime.parse("2022-02-02T19:30:00");
    private final static Long CREATED_BY = 1L;


    public static Event eventInit() {
        return Event.create(NAME,DESCRIPTION, VENUE, DATE_TIME, CREATED_BY);
    }

    public static EventDTO eventDTOInit() {
        return EventDTO.builder()
                .id(1L)
                .name(NAME)
                .description(DESCRIPTION)
                .venue(VENUE)
                .dateTime(DATE_TIME)
                .createdBy(CREATED_BY)
                .createdAt(LocalDateTime.now())
                .isCanceled(false)
                .build();
    }
}
