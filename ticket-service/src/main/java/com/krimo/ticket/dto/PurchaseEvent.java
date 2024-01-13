package com.krimo.ticket.dto;

import java.time.LocalDateTime;

public record PurchaseEvent(
        Long eventId,
        Long userId,
        LocalDateTime timestamp
) {
}
