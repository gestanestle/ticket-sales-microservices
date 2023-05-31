package com.krimo.ticket.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionAttendeeMap {

    private HashMap<Section, Integer> maxCapacity;
    private HashMap<Section, Integer> attendeeCount;
}
