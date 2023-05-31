package com.krimo.ticket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.ticket.data.Section;
import com.krimo.ticket.data.SectionAttendeeMap;
import com.krimo.ticket.data.Ticket;
import com.krimo.ticket.dto.TicketDTO;
import com.krimo.ticket.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;


public interface TicketPurchaseService {
    String purchaseTicket(TicketDTO ticketDTO);
    Ticket ticketBuild(TicketDTO ticketDTO);

}


@Service
@RequiredArgsConstructor
class TicketPurchaseServiceImpl implements TicketPurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(TicketPurchaseServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final RedisService redisService;
    private final TicketService ticketService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * When you register to an event, aka. purchase ticket, three processes happen:
     *
     * 1. We retrieve the count of both maxCapacity and attendeeCount to check if you can still register.
     * 2. If there's still slot for you, we increment the attendeeCount by 1 and save the details
     * that you provided upon ticket purchase.
     * 3. We return a serial code (ticketCode) to confirm your slot for Event E.
     *
     * @param ticketDTO the request body from user
     * @return ticketCode
     */
    @Override
    public synchronized String purchaseTicket(TicketDTO ticketDTO) {

        String eventCode = ticketDTO.getEventCode();
        Section section = ticketDTO.getSection();

        int maxCapacity = redisService.getSectionAttendeeMap(eventCode).getMaxCapacity().get(section);

        boolean isCountMapEmpty = redisService.getSectionAttendeeMap(eventCode).getAttendeeCount() == null;

        HashMap<Section, Integer> countMap = isCountMapEmpty ? new HashMap<>() :
                redisService.getSectionAttendeeMap(eventCode).getAttendeeCount();

        logger.info("ATTENDEE COUNT MAP: " + countMap.get(section));

        if (!countMap.containsKey(section)) {
            countMap.put(ticketDTO.getSection(), 1);
        } else {
            int count = countMap.get(section);
            if (countMap.get(section) >= maxCapacity) {
                throw new ApiRequestException("Ticket sold out.");
            }
            countMap.put(section, ++count);
        }

        // keep track of attendee count
        SectionAttendeeMap sectionAttendeeMap = redisService.getSectionAttendeeMap(eventCode);
        sectionAttendeeMap.setAttendeeCount(countMap);
        redisService.storeSectionAttendeeMap(eventCode, sectionAttendeeMap);

        Ticket ticket = ticketBuild(ticketDTO);
        return ticketService.saveTicket(ticket);
    }

    @Override
    public Ticket ticketBuild(TicketDTO ticketDTO) {
        return Ticket.builder()
                .eventCode(ticketDTO.getEventCode())
                .section(ticketDTO.getSection())
                .purchaserEmail(ticketDTO.getPurchaserEmail())
                .build();
    }
}

