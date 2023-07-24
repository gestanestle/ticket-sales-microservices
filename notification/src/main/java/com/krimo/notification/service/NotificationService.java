package com.krimo.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.message.payload.TicketPurchasePayload;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationService {
    void sendConfirmationMessage(String msgID, String msgPayload) throws Exception;
}
@Transactional
@Service
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final MessageSenderService senderService;
    private final ClientService clientService;
    private final static int MAX_RETRIES = 3;

    public void sendConfirmationMessage(String msgID, String msgPayload) throws Exception {
        TicketPurchasePayload payload = objectMapper.readValue(msgPayload, TicketPurchasePayload.class);

        Long userId = payload.purchasedBy();
        String email = null;

        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                email = clientService.getEmail(userId);
                break;
            } catch (FeignException e) {
                retries++;
            }
        }

        if (retries == MAX_RETRIES) {
            logger.debug("FEIGN ERROR: Max retries reached.");
            throw new Exception();
        }

        final String subject = "Confirmation: Successful Ticket Purchase";

        final String msg =
                """
                        Congratulations! Your ticket purchase for %s is confirmed.
                   
                        
                        Thank you for choosing to be a part of this memorable experience. We can't wait to see you there!
                        
                        """;

        senderService.sendMessage(email, subject, String.format(msg, payload.eventName()));
    }
    
}
