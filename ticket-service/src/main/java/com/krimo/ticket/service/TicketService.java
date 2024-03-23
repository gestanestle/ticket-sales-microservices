package com.krimo.ticket.service;

import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.models.Event;
import com.krimo.ticket.models.Ticket;
import com.krimo.ticket.repository.EventRepository;
import com.krimo.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

public interface TicketService {

    Long createTicket(Long id, TicketDTO dto);
    TicketDTO getTicket(Long id);
    List<TicketDTO> getTicketsByEvent(Long id);
    void updateTicket(Long id, TicketDTO dto);
    void deleteTicket(Long id);
}

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class TicketServiceImpl implements TicketService {
    
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    @Override
    public Long createTicket(Long id, TicketDTO dto) {
        Event event = eventRepository.findById(id).orElseThrow();
        log.info("Event retrieved: " + event);
        Ticket ticket = Ticket.create(event, dto.type(), dto.price(), dto.qtyStock());
        return ticketRepository.saveAndFlush(ticket).getTicketId();
    }

    @Override
    public TicketDTO getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        return mapToDTO(ticket);
    }

    @Override
    public List<TicketDTO> getTicketsByEvent(Long id) {
        return ticketRepository.getByEvent(id).stream().map(this::mapToDTO).toList();
    }

    @Override
    public void updateTicket(Long id, TicketDTO dto) {
        log.info("DTO: " + dto);

        Ticket ticket = ticketRepository.getReferenceById(id);
        log.info("Before: " + ticket);
        if (Objects.nonNull(dto.type())) ticket.setType(dto.type());
        if (Objects.nonNull(dto.price())) {
            if (ticket.getQtySold() > 0)
                throw new ApiRequestException(HttpStatus.BAD_REQUEST, "Cannot update ticket price. Tickets have already been sold.");
            ticket.setPrice(dto.price());
        }
        if (Objects.nonNull(dto.qtyStock())) {
            if (dto.qtyStock() <= ticket.getQtySold())
                throw new ApiRequestException(HttpStatus.BAD_REQUEST, "New quantity stock cannot be less than the current amount of ticket sold.");
            ticket.setQtyStock(dto.qtyStock());
        }
        ticketRepository.save(ticket);
        log.info("After: " + ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        if (ticketRepository.findById(id).isEmpty()) throw new ApiRequestException(HttpStatus.NO_CONTENT, null);
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(Ticket t) {
        return new TicketDTO(t.getTicketId(), t.getEvent().getEventId(), t.getType(), t.getPrice(), t.getQtyStock(), t.getQtySold());
    }
}


