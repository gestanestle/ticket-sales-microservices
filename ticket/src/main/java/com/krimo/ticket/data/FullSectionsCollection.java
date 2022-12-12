package com.krimo.ticket.data;

import lombok.Data;

import java.util.Collection;

@Data
public class FullSectionsCollection {

    private Collection<Section> sections;
}
