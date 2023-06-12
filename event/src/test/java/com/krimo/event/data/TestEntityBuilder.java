package com.krimo.event.data;

import com.krimo.event.dto.EventDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class TestEntityBuilder {

    private final static String VENUE = "Mall of Asia";
    private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.parse("2022-02-02T19:30:00");
    private final static String NAME = "The Eras Tour";
    private final static String DETAILS = "The Taylor Swift world tour concert after Midnights.";
    private final static String ORGANIZER = "Krimo";
    private final static String NEW_ORGANIZER = "Nestle";
    private final HashMap<Section, Integer> maxCapacity = new HashMap<>();


    public Event event() {
        map();
        return Event.builder()
                .name(NAME).venue(VENUE).dateTime(LOCAL_DATE_TIME)
                .details(DETAILS).organizer(ORGANIZER)
                .createdAt(LOCAL_DATE_TIME)
                .build();
    }

    public EventDTO eventDTO() {
        map();
        return new EventDTO(VENUE, LOCAL_DATE_TIME, TITLE, DETAILS, maxCapacity, ORGANIZER);
    }


    public void map() {
        maxCapacity.put(Section.VIP, 10);
        maxCapacity.put(Section.LOWER_BOX, 20);
        maxCapacity.put(Section.UPPER_BOX, 30);
        maxCapacity.put(Section.GEN_AD, 40);
    }
}
