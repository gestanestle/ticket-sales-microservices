package com.krimo.notification.dto;

import java.time.LocalDateTime;

public record PurchaseEvent(
        Long eventId,
        Long userId,
        LocalDateTime timestamp
) {
}
