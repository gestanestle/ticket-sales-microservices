package com.krimo.email.service;

import com.krimo.email.bean.EmailSenderService;
import com.krimo.email.dto.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailFormatter {

    private final EmailSenderService emailSenderService;


    public void emailFormatter(String ownerEmail, Event event) {

        final String sub = "Event Updates";

        final String msg =
                """
                        This is to inform you that the event with the code %1$s has been updated by the organizer. See below for the updated event details: \s
                        \s
                        Title: %2$s \s
                        Details; %3$s \s
                        Venue: %4$s \s
                        Date and Time: %5$s \s
                        \s
                        """;

        emailSenderService.sendMaiL(ownerEmail, sub,String.format(msg,
                                            event.getEventCode(),
                                            event.getTitle(),
                                            event.getDetails(),
                                            event.getVenue(),
                                            event.getDateTime()
                )
        );
    }
}
