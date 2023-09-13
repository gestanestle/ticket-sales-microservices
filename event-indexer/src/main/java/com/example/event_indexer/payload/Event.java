package com.example.event_indexer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Event(
        @JsonProperty("event_id") String id,
        @JsonProperty("event_name") String name,
        String description,
        String venue,
        @JsonProperty("date_time") String dateTime,
        @JsonProperty("created_by") Long createdBy,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("is_canceled") Boolean isCanceled
) {
}
