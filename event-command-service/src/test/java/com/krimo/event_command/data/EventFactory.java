package com.krimo.event_command.data;

import com.krimo.event_command.data.types.Status;
import com.krimo.event_command.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.HashSet;

public class EventFactory {

    private final static String NAME = "The Eras Tour";
    private final static String DESCRIPTION = "The Taylor Swift world tour concert after Midnights.";
    private final static String VENUE = "Mall of Asia";
    private final static LocalDateTime START_DATETIME = LocalDateTime.parse("2022-02-02T19:30:00");
    private final static LocalDateTime END_DATETIME = LocalDateTime.parse("2022-02-03T19:30:00");
    private final static String ORGANIZER = "krimeu";


    public static Event eventInit() {
        return Event.create(NAME, "", DESCRIPTION, VENUE, START_DATETIME, END_DATETIME, ORGANIZER, new HashSet<>());
    }

    public static EventDTO eventDTOInit() {
        return new EventDTO(NAME, "", DESCRIPTION, VENUE, START_DATETIME, END_DATETIME, ORGANIZER, new HashSet<>(), Status.ACTIVE);
    }

    public static EventDTO eventDTOUpdate(String venue, String desc) {
        return new EventDTO(NAME, "", desc, venue, START_DATETIME, END_DATETIME, ORGANIZER, new HashSet<>(), Status.ACTIVE);
    }
}
