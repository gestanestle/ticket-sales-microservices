package com.krimo.account.controller;

import com.krimo.account.dto.AccountDTO;
import com.krimo.account.dto.ResponseObject;
import com.krimo.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor @Slf4j
@RequestMapping("api/v3/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ResponseObject> createAccount(@RequestBody AccountDTO dto) {
        Long id = accountService.createAccount(dto);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully created.",
                201,
                id
        ), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAccounts(
            @RequestParam("pageNo") int pageNo,
            @RequestParam("pageSize") int pageSize
    ) {
        List<AccountDTO> accounts = accountService.getAccounts(pageNo, pageSize);
        return new ResponseEntity<>(ResponseObject.of(
                "Accounts successfully retrieved.",
                200,
                accounts
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseObject> getAccount(@PathVariable("id") Long id) {
        AccountDTO account = accountService.getAccount(id);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully retrieved.",
                200,
                account
        ), HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable("id") Long id,
                                                    @RequestBody AccountDTO dto) {
        accountService.updateAccount(id, dto);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully updated.",
                200,
                null
        ), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(ResponseObject.of(
                "Account successfully deleted.",
                200,
                null
        ), HttpStatus.OK);
    }

    @GetMapping(path = "{id}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(accountService.getUserEmail(id), HttpStatus.OK);
    }
}
