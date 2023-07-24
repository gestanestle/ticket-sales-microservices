package com.krimo.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.message.BrokerMessage;
import com.krimo.notification.repository.CacheService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class EventListener {

    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);
    private final ObjectMapper objectMapper;

    private final NotificationService notificationService;
    private final CacheService cacheService;

    @KafkaListener(topics = "outbox.event.ticket_purchase")
    public void consumeMessages(String ticketPurchase)  {
        logger.info("CONSUME MESSAGES() RECEIVED MSG");
        System.out.println("===========CONSUME MESSAGES() RECEIVED MSG===========");
        final String hashKey = "FAILED";

        // Deserialize broker message
        BrokerMessage message = null;

        try {
            message = objectMapper.readValue(ticketPurchase, BrokerMessage.class);
        }catch (JsonProcessingException e) {
            logger.debug("FAILED TO DESERIALIZE Broker Message.");
        }

        String msgID = message.id();

        // Idempotency check
        if (cacheService.isKeyPresent(msgID)) {
            return;
        }

        try {
            notificationService.sendConfirmationMessage(msgID, message.payload());
        } catch (Exception e) {
            logger.debug("Failed to send confirmation message");
            logger.debug(e.getMessage());
            cacheService.saveHash(hashKey, msgID, message.payload()); // save as hash if can't send confirmation
            return;
        }
        cacheService.saveKeyValue(msgID, ""); // otherwise save message ID as key

    }

}
