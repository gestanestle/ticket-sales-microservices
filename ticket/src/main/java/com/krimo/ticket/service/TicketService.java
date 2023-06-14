package com.krimo.ticket.service;

import com.krimo.ticket.data.EventCapacity;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.repository.EventCapacityRepository;
import com.krimo.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface TicketService {
    TicketDTO purchaseTicket(TicketDTO ticketDTO);
    TicketDTO getTicket(String ticketCode);
    Set<String> getEmailsSet(String eventCode);

}

@Service
@RequiredArgsConstructor
class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final EventCapacityRepository eventCapacityRepository;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public synchronized TicketDTO purchaseTicket(TicketDTO ticketDTO) {

        String eventCode = ticketDTO.getEventCode();
        String seat = ticketDTO.getSection().name();

        EventCapacity eventCapacity = eventCapacityRepository.getEventCapacity(eventCode, seat);

        int ac = eventCapacity.getAttendeeCount();
        int mc = eventCapacity.getMaxCapacity();

        if (ac >= mc) { throw new ApiRequestException("Ticket sold out."); }

        eventCapacityRepository.incrementAttendeeCount(ac + 1, eventCode, seat);

        return ticketDTOBuild(createTicket(ticketDTO));
    }

    private Ticket createTicket(TicketDTO ticketDTO) {
        // serial code generator
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }

        String ticketCode = stringBuilder.toString();

        Ticket ticket = Ticket.builder()
                        .eventCode(ticketDTO.getEventCode())
                        .ticketCode(ticketCode)
                        .section(ticketDTO.getSection())
                        .purchaserEmail(ticketDTO.getPurchaserEmail())
                        .createdAt(LocalDateTime.now())
                        .build();

        ticketRepository.saveTicket(ticket);
        return ticket;
    }

    @Override
    public TicketDTO getTicket(String ticketCode) {
        Ticket ticket = ticketRepository.getTicket(ticketCode);
        return ticketDTOBuild(ticket);
    }

    @Override
    public Set<String> getEmailsSet(String eventCode) {
        return new HashSet<>(ticketRepository.getAllEmails(eventCode));
    }

    private TicketDTO ticketDTOBuild(Ticket ticket) {
        return TicketDTO.builder()
                .eventCode(ticket.getEventCode())
                .ticketCode(ticket.getTicketCode())
                .section(ticket.getSection())
                .purchaserEmail(ticket.getPurchaserEmail())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

}