package com.krimo.notification.message;

import lombok.Getter;
import lombok.Setter;

public record BrokerMessage(String id, String payload) {
}
