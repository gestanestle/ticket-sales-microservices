package com.krimo.ticket.controller;

import com.krimo.ticket.dto.PurchaseRequest;
import com.krimo.ticket.dto.ResponseBody;
import com.krimo.ticket.models.Purchase;
import com.krimo.ticket.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v3/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    ResponseEntity<ResponseBody> purchaseTicket(@RequestBody PurchaseRequest request) {
        Set<Long> id = purchaseService.createPurchase(request);
        return new ResponseEntity<>(ResponseBody.of(
                "Purchase successful",
                HttpStatus.CREATED,
                Map.of("data", id)
        ), HttpStatus.CREATED);
    }

    @GetMapping(path = "{purchaseId}")
    ResponseEntity<ResponseBody> getTicket(@PathVariable("purchaseId") Long id) {
        Purchase purchase =  purchaseService.getPurchase(id);
        return new ResponseEntity<>(ResponseBody.of(
                "Purchase successful",
                HttpStatus.OK,
                Map.of("data", purchase)
        ), HttpStatus.OK);
    }

}
