package com.krimo.account.controller;

import com.krimo.account.dto.AccountDTO;
import com.krimo.account.dto.ResponseObject;
import com.krimo.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v3/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ResponseObject> createAccount(@RequestBody AccountDTO dto) {
        Long id = accountService.createAccount(dto);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully created.",
                HttpStatus.CREATED,
                Map.of("data", id)
        ), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAccounts() {
        List<AccountDTO> accounts = accountService.getAccounts();
        return new ResponseEntity<>(ResponseObject.of(
                "Accounts successfully retrieved.",
                HttpStatus.OK,
                Map.of("data", accounts)
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseObject> getAccount(@PathVariable("id") Long id) {
        AccountDTO account = accountService.getAccount(id);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully retrieved.",
                HttpStatus.OK,
                Map.of("data", account)
        ), HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable("id") Long id,
                                                    @RequestBody AccountDTO dto) {
        accountService.updateAccount(id, dto);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully updated.",
                HttpStatus.OK,
                null
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully deleted.",
                HttpStatus.OK,
                null
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(accountService.getUserEmail(id), HttpStatus.OK);
    }
}
