package com.krimo.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.message.BrokerMessage;
import com.krimo.notification.message.payload.TicketPurchasePayload;
import com.krimo.notification.repository.BrokerMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
class NotificationService {

    private final ObjectMapper objectMapper;
    private final ClientService clientService;
    private final MessageSenderService senderService;
    private final BrokerMessageRepository messageRepository;

    @KafkaListener(topics = "outbox.event.ticket_purchase")
    public void sendPurchaseConfirmation(String ticketPurchase) throws JsonProcessingException {

        BrokerMessage message = objectMapper.readValue(ticketPurchase, BrokerMessage.class);

        if (messageRepository.isKeyPresent(message.id())) { return; }

        TicketPurchasePayload payload = objectMapper.readValue(message.payload(), TicketPurchasePayload.class);

        String email = clientService.getEmail(payload.purchasedBy());
        String arg = payload.eventName();

        senderService.sendMessage(email, arg);
        messageRepository.saveMessage(message);
    }
}
