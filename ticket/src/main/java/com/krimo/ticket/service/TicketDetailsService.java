package com.krimo.ticket.service;

import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.TicketDetails;
import com.krimo.ticket.data.TicketDetailsPK;
import com.krimo.ticket.dto.TicketDetailsDTO;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.repository.TicketDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface TicketDetailsService {

    void setTicketDetails(Long eventId, TicketDetailsDTO ticketDetailsDTO);

    List<TicketDetailsDTO> getTicketDetailsByEvent(Long eventId);


}

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class TicketDetailsServiceImpl implements TicketDetailsService {

    private final TicketDetailsRepository ticketDetailsRepository;
    @Override
    public void setTicketDetails(Long eventId, TicketDetailsDTO ticketDetailsDTO) {

        log.debug("EVENT ID: " + eventId);

        Section section = ticketDetailsDTO.getSection();
        TicketDetailsPK pk = TicketDetailsPK.of(eventId, section);

        boolean isTicketDetailsPresent = ticketDetailsRepository.findById(pk).isPresent();

        TicketDetails ticketDetails;

        if (!isTicketDetailsPresent) {
            ticketDetails = TicketDetails.create(
                    pk,
                    ticketDetailsDTO.getPrice(),
                    ticketDetailsDTO.getTotalStock());
        }

        else {
            ticketDetails = ticketDetailsRepository.findById(pk).orElseThrow();

            if (ticketDetails.getTotalSold() != 0) {
                throw new ApiRequestException("Invalid request. Tickets have already been sold.");
            }

            if (ticketDetailsDTO.getPrice() != null) { ticketDetails.setPrice(ticketDetailsDTO.getPrice()); }
            if (ticketDetailsDTO.getTotalStock() != null) { ticketDetails.setTotalStock(ticketDetailsDTO.getTotalStock()); }
        }

        ticketDetailsRepository.save(ticketDetails);
    }

    @Override
    public List<TicketDetailsDTO> getTicketDetailsByEvent(Long eventId) {
        List<TicketDetailsDTO> ticketDetailsDTOList = new ArrayList<>();
        ticketDetailsRepository.getTicketDetailsByEvent(eventId)
                .forEach(ticketDetails -> ticketDetailsDTOList.add(mapToTicketDetailsDTO(ticketDetails)));

        return ticketDetailsDTOList;
    }

    // Reusable method - entity to dto mapping
    private TicketDetailsDTO mapToTicketDetailsDTO(TicketDetails ticketDetails) {
        return TicketDetailsDTO.builder()
                .eventId(ticketDetails.getPk().getEventId())
                .section(ticketDetails.getPk().getSection())
                .price(ticketDetails.getPrice())
                .totalStock(ticketDetails.getTotalStock())
                .totalSold(ticketDetails.getTotalSold())
                .build();
    }
}