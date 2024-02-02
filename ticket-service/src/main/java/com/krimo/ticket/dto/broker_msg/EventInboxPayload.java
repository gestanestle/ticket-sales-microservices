package com.krimo.ticket.dto.broker_msg;

public record EventInboxPayload(
        Long eventId,
        Boolean isActive
) {
}
