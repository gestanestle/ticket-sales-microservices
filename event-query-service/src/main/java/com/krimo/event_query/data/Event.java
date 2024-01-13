package com.krimo.event_query.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public record Event (
        Long id,
        String name,
        String banner,

        String description,
        String venue,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String organizer,
        Set<String> tags,
        Status status
) {

}
