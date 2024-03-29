package com.krimo.event_query.data;

import java.time.LocalDateTime;
import java.util.Set;

public record Event (
        Long event_id,
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
