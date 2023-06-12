package com.krimo.ticket.service;

import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    void saveTicket(Ticket ticket);
    List<String> getEmailsList(String eventCode);
}

@Service
@RequiredArgsConstructor
class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Ticket createTicket(Ticket ticket) {
        // TODO: FOR TESTING PURPOSES ONLY.
        saveTicket(ticket);
        return ticket;
    }

    @Override
    public void saveTicket(Ticket ticket) {
        // serial code generator
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }

        String ticketCode = stringBuilder.toString();

        ticket.setTicketCode(ticketCode);
        ticket.setCreatedAt(LocalDateTime.now());

        ticketRepository.saveTicket(ticket);

    }

    @Override
    public List<String> getEmailsList(String eventCode) {
        return ticketRepository.getAllEmails(eventCode).stream().toList();
    }
}