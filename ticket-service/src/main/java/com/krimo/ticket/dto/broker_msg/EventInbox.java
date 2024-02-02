package com.krimo.ticket.dto.broker_msg;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventInbox (
        UUID id,
        String topic,
        String payload,
        LocalDateTime timestamp
) {

}
