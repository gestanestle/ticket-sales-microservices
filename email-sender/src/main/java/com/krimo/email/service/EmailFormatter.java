package com.krimo.email.service;

import com.krimo.email.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface EmailFormatter {
    void formatMail(String ownerEmail, EventDTO event);
}
@Service
@RequiredArgsConstructor
class EmailFormatterImpl implements EmailFormatter {

    private final MailerService mailerService;

    @Override
    public void formatMail(String ownerEmail, EventDTO event) {

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

        mailerService.sendMaiL(ownerEmail, sub, String.format(msg,
                                            event.getTitle(),
                                            event.getEventCode(),
                                            event.getDetails(),
                                            event.getVenue(),
                                            event.getDateTime()
                )
        );
    }
}
