package com.krimo.ticket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krimo.ticket.data.MockData;
import com.krimo.ticket.dto.PurchaseRequest;
import com.krimo.ticket.models.Purchase;
import com.krimo.ticket.models.PurchaseStatus;
import com.krimo.ticket.service.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PurchaseController.class)
@Slf4j
public class PurchaseControllerTest {

    @MockBean
    private PurchaseService purchaseService;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void purchaseTicket() throws Exception {

        String dtoJson = "";

        try {
            objectMapper.registerModule(new JavaTimeModule());
            dtoJson = objectMapper.writeValueAsString(new PurchaseRequest(1L, 1, PurchaseStatus.BOOKED, 1L));
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize message.");
        }

        when(purchaseService.createPurchase(any(PurchaseRequest.class))).thenReturn(Set.of(1L));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8082/api/v3/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.data[0]", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Purchase successful.")));
    }

    @Test
    void getTicket() throws Exception {
        Purchase purchase = MockData.purchaseInit();
        purchase.setPurchaseId(1L);
        when(purchaseService.getPurchase(anyLong())).thenReturn(purchase);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("http://localhost:8082/api/v3/purchases/%s",1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.data.purchaseId", org.hamcrest.Matchers.is(1)))
                .andExpect(jsonPath("$.message",  org.hamcrest.Matchers.is("Purchase details successfully retrieved.")));
    }
}
