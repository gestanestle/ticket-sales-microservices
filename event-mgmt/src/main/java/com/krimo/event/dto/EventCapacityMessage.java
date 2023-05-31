package com.krimo.event.dto;

import com.krimo.event.data.Section;
import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCapacityMessage {

    private String eventCode;
    private HashMap<Section, Integer> maxCapacity;

}
