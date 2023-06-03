package com.krimo.ticket.service;

import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    String saveTicket(Ticket ticket);
    List<String> emailsList(String eventCode);
}

@Service
@RequiredArgsConstructor
class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String saveTicket(Ticket ticket) {
        // serial code generator
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }

        String ticketCode = stringBuilder.toString();

        ticket.setTicketCode(ticketCode);
        ticket.setPurchaseDateTime(LocalDateTime.now());
        ticketRepository.save(ticket);

        return ticketCode;
    }

    @Override
    public List<String> emailsList(String eventCode) {
        return ticketRepository.getEmails(eventCode).stream().toList();
    }
}
