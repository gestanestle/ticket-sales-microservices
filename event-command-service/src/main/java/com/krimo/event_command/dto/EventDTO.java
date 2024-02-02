package com.krimo.event_command.dto;

import com.krimo.event_command.data.types.Status;

import java.time.LocalDateTime;
import java.util.Set;

public record EventDTO (
        String name,
        String banner,

        String description,
        String venue,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String organizer,
        Set<String> tags,
        Status status
) {}



