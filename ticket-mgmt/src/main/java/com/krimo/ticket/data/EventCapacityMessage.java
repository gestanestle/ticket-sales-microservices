package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventCapacityMessage {
    private String eventCode;
    private HashMap<Section, Integer> maxCapacity;

}

