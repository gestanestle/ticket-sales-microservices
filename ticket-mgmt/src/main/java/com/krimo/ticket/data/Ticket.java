package com.krimo.ticket.data;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @SequenceGenerator(name = "er_seq", sequenceName = "er_seq", allocationSize = 1)
    @GeneratedValue(generator = "er_seq", strategy = GenerationType.AUTO)
    private Long id;
    private String eventCode;
    private String ticketCode;
    private Section section;
    private LocalDateTime purchaseDateTime;
    private String purchaserEmail;

}