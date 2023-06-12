package com.krimo.email.dto;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {

    private Long id;
    private String eventCode;
    private String name;
    private String venue;
    private LocalDateTime dateTime;
    private String details;
    private String organizer;
    private LocalDateTime createdAt;

}
