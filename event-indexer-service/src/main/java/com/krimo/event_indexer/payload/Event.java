package com.krimo.event_indexer.payload;

import java.util.Set;

public record Event (
        Long id,
        String name,
        String banner,

        String description,
        String venue,
        String startDateTime,
        String endDateTime,
        String organizer,
        Set<String> tags,
        Status status
) {

}
