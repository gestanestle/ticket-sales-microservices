package com.krimo.event.service;

import com.krimo.event.data.Event;
import com.krimo.event.data.Section;
import com.krimo.event.data.TicketDetails;
import com.krimo.event.data.TicketDetailsPK;
import com.krimo.event.dto.TicketDetailsDTO;
import com.krimo.event.repository.EventRepository;
import com.krimo.event.repository.TicketDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface TicketDetailsService {

    void setTicketDetails(Long eventId, TicketDetailsDTO ticketDetailsDTO);

    List<TicketDetailsDTO> getTicketDetailsByEvent(Long eventId);

    void updateTicketDetails(Long eventId, String section, TicketDetailsDTO ticketDetailsDTO);

    void deleteTicketDetails(Long eventId, String section);
}

@Service
@Transactional
@RequiredArgsConstructor
class TicketDetailsServiceImpl implements TicketDetailsService {

    private final EventRepository eventRepository;
    private final TicketDetailsRepository ticketDetailsRepository;

    @Override
    public void setTicketDetails(Long eventId, TicketDetailsDTO ticketDetailsDTO) {

        TicketDetails ticketDetails = TicketDetails.create(
                            pk(event(eventId), ticketDetailsDTO.getSection()),
                            ticketDetailsDTO.getPrice(),
                            ticketDetailsDTO.getTotalStock());

        ticketDetailsRepository.save(ticketDetails);
    }

    @Override
    public List<TicketDetailsDTO> getTicketDetailsByEvent(Long eventId) {
        List<TicketDetailsDTO> ticketDetailsDTOList = new ArrayList<>();
        ticketDetailsRepository.getTicketDetailsByEvent(event(eventId))
                .forEach(ticketDetails -> ticketDetailsDTOList.add(mapToTicketDetailsDTO(ticketDetails)));

        return ticketDetailsDTOList;
    }

    @Override
    public void updateTicketDetails(Long eventId, String section, TicketDetailsDTO ticketDetailsDTO) {

        TicketDetails ticketDetails = ticketDetailsRepository.getReferenceById(pk(event(eventId), Section.valueOf(section.toUpperCase())));

        if (ticketDetailsDTO.getPrice() != null) { ticketDetails.setPrice(ticketDetailsDTO.getPrice()); }
        if (ticketDetailsDTO.getTotalStock() != null) { ticketDetails.setTotalStock(ticketDetailsDTO.getTotalStock()); }

        ticketDetailsRepository.save(ticketDetails);
    }

    @Override
    public void deleteTicketDetails(Long eventId, String section) {
        ticketDetailsRepository.deleteById(pk(event(eventId), Section.valueOf(section.toUpperCase())));
    }

    private Event event(Long id) { return eventRepository.getReferenceById(id); }

    private TicketDetailsPK pk(Event event, Section section) {
        return TicketDetailsPK.of(event, section);
    }

    // Reusable method - entity to dto mapping
    private TicketDetailsDTO mapToTicketDetailsDTO(TicketDetails ticketDetails) {
        return TicketDetailsDTO.builder()
                .eventId(ticketDetails.getPk().getEvent().getId())
                .section(ticketDetails.getPk().getSection())
                .price(ticketDetails.getPrice())
                .totalStock(ticketDetails.getTotalStock())
                .remainingStock(ticketDetails.getRemainingStock())
                .build();
    }
}