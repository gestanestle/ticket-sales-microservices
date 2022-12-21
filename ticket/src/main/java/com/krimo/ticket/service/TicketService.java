package com.krimo.ticket.service;

import com.krimo.ticket.client.EventClient;
import com.krimo.ticket.data.Event;
import com.krimo.ticket.dto.*;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.repository.TicketRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public synchronized ReturnObject buyTicket(CustomerDTO customerDTO) {

        TicketDTO ticketDTO = new TicketDTO(customerDTO.getEventCode(),customerDTO.getSection());

        try {
            eventClient.addAttendees(ticketDTO.getEventCode(), ticketDTO);
        } catch (FeignException.BadRequest e) {
            return new ReturnObject(String.format("Section %s is already full.", customerDTO.getSection()));
        } catch (FeignException e) {
            return new ReturnObject("Sorry, we cannot process your request as of the moment. Please come back later.");
        }

        String ticketCode = UUID.randomUUID().toString();

        Ticket ticket = Ticket.builder()
                .ticketCode(ticketCode)
                .eventCode(customerDTO.getEventCode())
                .section(customerDTO.getSection())
                .purchaseDateTime(LocalDateTime.now())
                .customerEmail(customerDTO.getCustomerEmail())
                .build();

        Collection<Ticket> tickets = new ArrayList<>();

        Event event = ticketRepository.findByEventCode(customerDTO.getEventCode()).isPresent()
                ? ticketRepository.findByEventCode(customerDTO.getEventCode()).get()
                : new Event(customerDTO.getEventCode(), tickets);

        event.setEventCode(event.getEventCode());
        tickets = event.getTickets();
        tickets.add(ticket);
        event.setTickets(tickets);
        ticketRepository.save(event);

        return new ReturnObject(ticket);
    }

    public EventList allEvents () {
        return new EventList(ticketRepository.findAll().stream().toList());
    }

    public TicketList eventTickets(String eventCode) {

        TicketList ticketList = new TicketList();

        try {
            ticketList.setTicketList(ticketRepository.findByEventCode(eventCode).get().getTickets().stream().toList());
        }catch (NoSuchElementException e) {
            return null;
        }
        return ticketList;

    }
}
