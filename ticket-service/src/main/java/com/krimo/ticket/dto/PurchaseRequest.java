package com.krimo.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krimo.ticket.models.PurchaseStatus;

public record PurchaseRequest(
        Long ticketId,
        Integer quantity,
        @JsonProperty("status")
        PurchaseStatus status,
        Long customerId
) {
}
