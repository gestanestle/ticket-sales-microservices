package com.krimo.event.data;

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
    @Column(name = "remaining_stock", updatable = false)
    private Integer remainingStock;

    public TicketDetails(TicketDetailsPK pk, Integer price, Integer totalStock) {
        this.pk = pk;
        this.price = price;
        this.totalStock = totalStock;
    }

    public static TicketDetails create(TicketDetailsPK pk, Integer price, Integer totalStock) {
        return new TicketDetails(pk, price, totalStock);
    }
}
