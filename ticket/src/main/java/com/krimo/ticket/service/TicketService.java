package com.krimo.ticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.dao.EventDAO;
import com.krimo.ticket.data.Outbox;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.payload.TicketPurchasePayload;
import com.krimo.ticket.repository.OutboxRepository;
import com.krimo.ticket.repository.TicketDetailsRepository;
import com.krimo.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

public interface TicketService {

    Long purchaseTicket(TicketDTO ticketDTO);
    TicketDTO viewTicket(Long id);
    void cancelPurchase(Long id);
}

@Service
@RequiredArgsConstructor
@Transactional
class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketDetailsRepository ticketDetailsRepository;
    private final OutboxRepository outboxRepository;
    private final EventDAO eventDAO;
    private final ObjectMapper objectMapper;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Long purchaseTicket(TicketDTO ticketDTO) {

        if (eventDAO.isCanceled(ticketDTO.getEventId())) { throw new ApiRequestException("Event has been canceled."); }

        int sold = ticketRepository.getSold(
                ticketDTO.getEventId(),
                ticketDTO.getSection()
        );

        int stock = ticketDetailsRepository.getStock(
                ticketDTO.getEventId(),
                ticketDTO.getSection()
        );

        if (sold >= stock) { throw new ApiRequestException("Sold out."); }

        Long id = createTicket(ticketDTO);
        saveToOutbox(ticketDTO.getEventId(), ticketDTO.getPurchasedBy());

        return id;
    }

    @Override
    public TicketDTO viewTicket(Long id) {

        Ticket ticket = getTicket(id);
        return mapToTicketDTO(ticket);
    }

    @Override
    public void cancelPurchase(Long id) {
        ticketRepository.deleteById(id);
    }

    private Long createTicket(TicketDTO ticketDTO) {
        
        // serial code generator
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }

        String ticketCode = stringBuilder.toString();
        
        Ticket ticket = Ticket.create(
                ticketDTO.getEventId(),
                ticketDTO.getSection(),
                ticketCode,
                ticketDTO.getPurchasedBy()
        );
        return ticketRepository.saveAndFlush(ticket).getId();
    }

    private void saveToOutbox(Long eventId, Long purchasedBy) {

        String topic = "ticket_purchase";
        String payloadJson;
        String eventName = eventDAO.getEventName(eventId).orElseThrow();

        try {
            payloadJson = objectMapper.writeValueAsString(new TicketPurchasePayload(eventName, purchasedBy));
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to serialize message.");
        }

        outboxRepository.save(Outbox.publish(topic, payloadJson));

    }

    private Ticket getTicket(Long id) { return ticketRepository.findById(id).orElseThrow(); }

    private TicketDTO mapToTicketDTO(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .eventId(ticket.getEventId())
                .section(ticket.getSection())
                .ticketCode(ticket.getTicketCode())
                .purchasedBy(ticket.getPurchasedBy())
                .purchasedAt(ticket.getPurchasedAt())
                .build();
    }
}
