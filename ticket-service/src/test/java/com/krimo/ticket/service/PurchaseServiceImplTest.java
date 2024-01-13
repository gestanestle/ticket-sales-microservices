package com.krimo.ticket.service;

import com.krimo.ticket.data.MockData;
import com.krimo.ticket.dto.PurchaseEvent;
import com.krimo.ticket.dto.PurchaseRequest;
import com.krimo.ticket.exception.ApiRequestException;
import com.krimo.ticket.models.Purchase;
import com.krimo.ticket.models.PurchaseStatus;
import com.krimo.ticket.models.Ticket;
import com.krimo.ticket.repository.PurchaseRepository;
import com.krimo.ticket.repository.TicketRepository;
import com.krimo.ticket.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock private KafkaTemplate<String, String> kafka;
    @Mock private PurchaseRepository purchaseRepository;
    @Mock private TicketRepository ticketRepository;
    @InjectMocks @Autowired
    private PurchaseServiceImpl purchaseService;

    @Captor
    ArgumentCaptor<Purchase> purchaseCaptor;

    Ticket ticket = MockData.ticketInit();
    PurchaseRequest req = MockData.purchaseReq();
    Purchase purchase = MockData.purchaseInit();

    @BeforeEach
    void setUp() {
        purchaseService = new PurchaseServiceImpl(kafka, purchaseRepository, ticketRepository);
    }

    @Test
    void shouldCreatePurchase() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(purchaseRepository.saveAndFlush(purchaseCaptor.capture()))
                .thenReturn(purchase);

        purchaseService.createPurchase(req);

        verify(purchaseRepository, times(req.quantity())).saveAndFlush(any(Purchase.class));
        verify(ticketRepository, times(3)).findById(anyLong());
        verify(kafka, times(1)).send(anyString(), anyString());
        assertThat(purchaseCaptor.getValue()).usingRecursiveComparison().ignoringFields("purchaseId", "ticketCode", "createdAt").isEqualTo(purchase);
    }

    @Test
    void shouldErrorWhenSoldOut() {
        ticket.setQtySold(ticket.getQtyStock());
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));

        assertThatThrownBy( () -> purchaseService.createPurchase(req))
                .isInstanceOf(ApiRequestException.class).hasMessageContaining("Ticket already sold out.");

    }

    @Test
    void getPurchase() {
        when(purchaseRepository.findById(1L)).thenReturn(Optional.ofNullable(purchase));
        assertThat(purchaseService.getPurchase(1L)).isEqualTo(purchase);
    }

    @Test
    void updatePurchase() {
        PurchaseRequest updateReq =  new PurchaseRequest(1L, null, PurchaseStatus.CANCELED, 1L);
        when(purchaseRepository.findById(1L)).thenReturn(Optional.ofNullable(purchase));
        purchaseService.updatePurchase(1L, updateReq);
        verify(purchaseRepository, times(1)).save(purchaseCaptor.capture());
        assertThat(purchaseCaptor.getValue().getStatus()).isEqualTo(updateReq.status());
    }
}