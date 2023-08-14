package com.krimo.ticket.data;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Entity
@Table(name = "ticket_details")
public class TicketDetails {

    @EmbeddedId
    private TicketDetailsPK pk;
    private Integer price;
    @Column(name = "total_stock")
    private Integer totalStock;
    @Column(name = "total_sold")
    private Integer totalSold;
    public static TicketDetails create(TicketDetailsPK pk, Integer price, Integer totalStock) {
        return new TicketDetails(pk, price, totalStock, 0);
    }
}
