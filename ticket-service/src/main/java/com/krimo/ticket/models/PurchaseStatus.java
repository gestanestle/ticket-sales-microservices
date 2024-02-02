package com.krimo.ticket.models;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.stream;

public enum PurchaseStatus {
    BOOKED, CANCELED, REFUNDED;

    static final Set<String> set = new HashSet<>();

    static {
        stream(PurchaseStatus.values()).forEach(purchase -> set.add(purchase.name()));
    }

    public static boolean has(String item) {
        return set.contains(item);
    }
}
