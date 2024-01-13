package com.krimo.ticket.service;

import com.krimo.ticket.dto.PurchaseEvent;
import com.krimo.ticket.dto.PurchaseRequest;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.models.Purchase;
import com.krimo.ticket.models.PurchaseStatus;
import com.krimo.ticket.models.Ticket;
import com.krimo.ticket.repository.PurchaseRepository;
import com.krimo.ticket.repository.TicketRepository;
import com.krimo.ticket.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface PurchaseService {

    Set<Long> createPurchase(PurchaseRequest req);
    Purchase getPurchase(Long id);
    void updatePurchase(Long id, PurchaseRequest req);
}

@Service
@RequiredArgsConstructor @Slf4j
@Transactional(isolation = Isolation.SERIALIZABLE)
class PurchaseServiceImpl implements PurchaseService {

    private static final String TOPIC = "ticket-purchase";

    private final KafkaTemplate<String, String> kafka;
    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;

    @Override
    public Set<Long> createPurchase(PurchaseRequest req) {

        // 1. Is event ticket present?
        if (ticketRepository.findById(req.ticketId()).isEmpty()) throw new ApiRequestException(HttpStatus.BAD_REQUEST, "Ticket does not exist.");
        Ticket ticket = ticketRepository.findById(req.ticketId()).get();
        log.info("Ticket retrieved: " + ticket);

        // 2. Is event ticket sold out?
        if (ticket.getQtySold() >= ticket.getQtyStock()
                && req.quantity() > (ticket.getQtyStock() - ticket.getQtySold()))
            throw new ApiRequestException(HttpStatus.BAD_REQUEST, "Ticket already sold out.");

        // 3. Process ticket purchase if not sold out.
        Set<Long> iDs = new HashSet<>();
        int i = 1;
        while (i <= req.quantity()) {
            String ticketCode = Utils.generateSerialCode();
            Purchase purchase = Purchase.create(ticketCode, ticket, PurchaseStatus.BOOKED, req.customerId());
            Long id = purchaseRepository.saveAndFlush(purchase).getPurchaseId();
            iDs.add(id);
            log.info("Ticket " + i + "/" + req.quantity() + ": " + ticketCode);
            i++;
        }

        // 4. Publish to kafka
        Long eventId = ticket.getEvent().getEventId();
        String message = Utils.writeToJson(new PurchaseEvent(eventId, req.customerId(), LocalDateTime.now()));
        kafka.send(TOPIC, message);

        return iDs;
    }

    @Override
    public Purchase getPurchase(Long id) {
        return purchaseRepository.findById(id).orElseThrow();
    }

    @Override
    public void updatePurchase(Long id, PurchaseRequest req) {
        // Only status can be changed after purchase [CANCELED, REFUNDED]
        if (!PurchaseStatus.has(String.valueOf(req.status()))) throw new ApiRequestException(HttpStatus.BAD_REQUEST, "Invalid status input.");
        Purchase purchase = purchaseRepository.findById(id).orElseThrow();
        purchase.setStatus(req.status());
        purchaseRepository.save(purchase);
    }
}
