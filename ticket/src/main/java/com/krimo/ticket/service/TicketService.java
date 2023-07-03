package com.krimo.ticket.service;

import com.krimo.ticket.dao.TicketDetailsDAO;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
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
    private final TicketDetailsDAO ticketDetailsDAO;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Long purchaseTicket(TicketDTO ticketDTO) {

        Long eventId = ticketDTO.getEventId();
        String section = ticketDTO.getSection().name();

        if (ticketDetailsDAO.isSoldOut(eventId, section)) { throw new ApiRequestException("Sold out."); }
        ticketDetailsDAO.incrementTotalSold(eventId, section);
        return createTicket(ticketDTO);
    }

    @Override
    public TicketDTO viewTicket(Long id) {

        Ticket ticket = getTicket(id);
        return mapToTicketDTO(ticket);
    }

    @Override
    public void cancelPurchase(Long id) {

        Long eventId = getTicket(id).getEventId();
        String section = getTicket(id).getSection().name();

        ticketRepository.deleteById(id);
        ticketDetailsDAO.decrementTotalSold(eventId, section);
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
