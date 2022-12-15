package com.krimo.ticket.dto;

import com.krimo.ticket.data.Section;
import lombok.Data;

import java.util.Collection;

@Data
public class FullSectionsCollection {

    private Collection<Section> sections;
}
