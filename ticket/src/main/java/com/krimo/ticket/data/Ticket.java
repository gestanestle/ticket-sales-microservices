package com.krimo.ticket.data;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    private String ticketCode;
    private String eventCode;
    private Section section;
    private LocalDateTime purchaseDateTime;
    private String owner;

}
