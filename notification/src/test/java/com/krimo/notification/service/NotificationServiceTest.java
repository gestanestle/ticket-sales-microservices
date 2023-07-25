package com.krimo.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.notification.message.BrokerMessage;
import com.krimo.notification.message.payload.TicketPurchasePayload;
import com.krimo.notification.repository.BrokerMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock private ObjectMapper objectMapper;
    @Mock private MessageSenderService senderService;
    @Mock private ClientService clientService;
    @Mock private BrokerMessageRepository messageRepository;
    @InjectMocks @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(objectMapper, senderService, clientService, messageRepository);
    }

    @Test
    void sendPurchaseConfirmation() throws JsonProcessingException{
        String msgID = "9c308a66-715e-458d-9a13-344a09b09849v";
        String eventName  = "The Eras Tour";

        String ticketPurchase =
                "{\"payload\":\"{\\\"eventName\\\":\\\"The Eras Tour\\\",\\\"purchasedBy\\\":1}\",\"id\":\"9c308a66-715e-458d-9a13-344a09b09849\"}\n";
        String payloadJSON =
                "{\"payload\":\"{\\\"eventName\\\":\\\"The Eras Tour\\\",\\\"purchasedBy\\\":1}\"";
        String email = "xy.nessy@gmail.com";

        TicketPurchasePayload payload = new TicketPurchasePayload(eventName, 1L);
        BrokerMessage message = new BrokerMessage(msgID, payloadJSON);

        when(objectMapper.readValue(ticketPurchase, BrokerMessage.class)).thenReturn(message);
        when(messageRepository.isKeyPresent(message.id())).thenReturn(false);
        when(objectMapper.readValue(payloadJSON, TicketPurchasePayload.class)).thenReturn(payload);
        when(clientService.getEmail(1L)).thenReturn(email);

        notificationService.sendPurchaseConfirmation(ticketPurchase);

        verify(clientService, times(1)).getEmail(1L);
        verify(senderService, times(1)).sendMessage(email, eventName);

        ArgumentCaptor<BrokerMessage> captor = ArgumentCaptor.forClass(BrokerMessage.class);
        verify(messageRepository).saveMessage(captor.capture());

        assertEquals(captor.getValue().id(), msgID);
        assertEquals(captor.getValue().payload(), payloadJSON);
    }
}
