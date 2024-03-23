package com.krimo.account.service;

import com.krimo.account.data.AccountFactory;
import com.krimo.account.dto.AccountDTO;
import com.krimo.account.model.Account;
import com.krimo.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private CacheManager cacheManager;
    @Mock private ObjectMapper objectMapper;
    @Autowired @InjectMocks
    private AccountServiceImpl accountService;

    @Captor ArgumentCaptor<Account> captor;

    Account account = AccountFactory.accountInit();
    AccountDTO dto = AccountFactory.dtoInit();

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository, cacheManager, objectMapper);
    }

    private Optional<Account> getAccounts(String title) {
        return ofNullable(cacheManager.getCache("accounts")).map(c -> c.get(title, Account.class));
    }

    @Test
    void createAccount() {
        when(accountRepository.saveAndFlush(captor.capture())).thenReturn(account);
        accountService.createAccount(dto);
        verify(accountRepository, times(1)).saveAndFlush(captor.getValue());
        assertThat(captor.getValue()).usingRecursiveComparison()
                .ignoringFields("registeredAt")
                .isEqualTo(account);

    }

    @Test
    void updateAccount() {
        AccountDTO updateDTO = AccountFactory.updateDtoInit();

        when(accountRepository.findById(anyLong())).thenReturn(ofNullable(account));

        accountService.updateAccount(1L, dto);
        account.setLastName(updateDTO.getLastName());
        verify(accountRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison()
                .ignoringFields("registeredAt")
                .isEqualTo(account);

        // TODO: WRITE TESTS FOR CACHE

    }

    @Test
    void getAccount() {
    }

    @Test
    void getAccounts() {
    }

    @Test
    void getUserEmail() {
    }

    @Test
    void deleteAccount() {
    }
}