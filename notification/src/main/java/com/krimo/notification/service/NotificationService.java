package com.krimo.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.exception.ApiRequestException;
import com.krimo.notification.message.BrokerMessage;
import com.krimo.notification.message.payload.TicketPurchasePayload;
import com.krimo.notification.repository.BrokerMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
class NotificationService {

    private final ObjectMapper objectMapper;
    private final MessageSenderService senderService;
    private final ClientService clientService;
    private final BrokerMessageRepository messageRepository;

    @KafkaListener(topics = "outbox.event.ticket_purchase")
    public void sendPurchaseConfirmation(String ticketPurchase) {

        BrokerMessage message;

        try {
            message = objectMapper.readValue(ticketPurchase, BrokerMessage.class);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to deserialize broker message.");
        }

        if (messageRepository.isKeyPresent(message.id())) { return; }

        TicketPurchasePayload payload;
        try {
            payload = objectMapper.readValue(message.payload(), TicketPurchasePayload.class);
        } catch (JsonProcessingException e) {
            throw new ApiRequestException("Unable to deserialize broker message payload.");
        }

        String email = clientService.getEmail(payload.purchasedBy());
        String arg = payload.eventName();

        senderService.sendMessage(email, arg);
        messageRepository.saveMessage(message);
    }
}
