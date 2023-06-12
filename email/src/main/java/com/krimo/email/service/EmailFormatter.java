package com.krimo.email.service;

import com.krimo.email.dto.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface EmailFormatter {
    void formatMail(String ownerEmail, Event event);
}

@Service
@RequiredArgsConstructor
class EmailFormatterImpl implements EmailFormatter {

    private final Mailer mailer;

    @Override
    public void formatMail(String purchaserEmail, Event event) {

        final String sub = "Event Updates";

        final String msg =
                """
                        This is to inform you that the '%1$s' has been updated by the organizer. See below for the updated event details: \s
                        \s
                        Event code: %2$s \s
                        Details: %3$s \s
                        Venue: %4$s \s
                        Date and Time: %5$s \s
                        \s
                        """;

        mailer.sendMaiL(purchaserEmail, sub, String.format(msg,
                                            event.getName(),
                                            event.getEventCode(),
                                            event.getDetails(),
                                            event.getVenue(),
                                            event.getDateTime()
                )
        );
    }
}
