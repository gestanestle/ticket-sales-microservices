package com.krimo.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.account.exception.ApiRequestException;
import com.krimo.account.model.Account;
import com.krimo.account.dto.AccountDTO;
import com.krimo.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Long createAccount(AccountDTO dto);
    void updateAccount(Long id, AccountDTO dto);
    AccountDTO getAccount(Long id);
    List<AccountDTO> getAccounts(int pageNo, int pageSize);
    String getUserEmail (Long id);

    void deleteAccount(Long id);
}

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
class AccountServiceImpl implements AccountService {

    private static final String CACHE_NAME = "accounts";
    private static final String CACHE_ERR = "Cache not found.";
    private final AccountRepository accountRepository;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    @Override
    public Long createAccount(AccountDTO dto) {
        log.info("Create account: " + dto);
        Account account = Account.create(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getBirthDate()
        );

        Long id =  accountRepository.saveAndFlush(account).getId();

        log.info("Created account with ID " + id);
        return id;
    }

    @Override
    public void updateAccount(Long id, AccountDTO dto) {
        log.info("Update account: " + dto);
        Account account = accountRepository.findById(id).orElseThrow();

        if (dto.getEmail() != null) account.setEmail(dto.getEmail());
        if (dto.getPhone() != null) account.setPhone(dto.getPhone());
        if (dto.getLastName() != null) account.setLastName(dto.getLastName());
        if (dto.getFirstName() != null) account.setFirstName(dto.getFirstName());
        if (dto.getBirthDate() != null) account.setBirthDate(dto.getBirthDate());

        accountRepository.save(account);
        log.info("Updated account with ID " + id);

        Cache accounts = cacheManager.getCache(CACHE_NAME);
        if (accounts == null) {
            log.error(CACHE_ERR);
            return;
        }

        try {
            AccountDTO dtoToCache = mapToDTO(account);
            String dtoCache = objectMapper.writeValueAsString(dtoToCache);
            accounts.put(id, dtoCache);
            log.info("Cache update: " + id);
        }catch (JsonProcessingException e) {
            log.error("Error serializing to cache", e);
            throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Cannot serialize cache.");
        }

    }

    @Override
    public AccountDTO getAccount(Long id) {
        Cache accounts = cacheManager.getCache(CACHE_NAME);
        if (accounts == null) {
            log.error(CACHE_ERR);
            return mapToDTO(getAccFromDB(id));
        }

        return Optional.ofNullable(accounts.get(id))
                .map(Cache.ValueWrapper::get)
                .map(o -> {
                    try {
                        return objectMapper.readValue((String) o, AccountDTO.class);
                    } catch (IOException e) {
                        log.error("Error deserializing from cache", e);
                        throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Cannot deserialize from cache.");
                    }
                })
                .orElseGet(() -> {
                    try {
                        AccountDTO dto = mapToDTO(getAccFromDB(id));
                        String dtoCache = objectMapper.writeValueAsString(dto);
                        accounts.put(id, dtoCache);
                        log.info("Cache create: " + id);
                        return dto;
                    } catch (JsonProcessingException e) {
                        log.error("Error serializing to cache", e);
                        throw new ApiRequestException(HttpStatus.SERVICE_UNAVAILABLE, "Cannot serialize cache.");
                    }
                });
    }


    @Override
    public List<AccountDTO> getAccounts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return accountRepository.findAll(pageable).map(this::mapToDTO).stream().toList();
    }

    @Override
    public String getUserEmail (Long id) {
        String email = accountRepository.getReferenceById(id).getEmail();
        log.info("Retrieved user email: " + email);
        return email;
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
        log.info("Deleted account with ID " + id);
        Cache accounts = cacheManager.getCache(CACHE_NAME);
        if (accounts == null) {
            log.error(CACHE_ERR);
            return;
        }
        accounts.evict(id);
        log.info("Cache evict: " + id);
    }

    private Account getAccFromDB(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    private AccountDTO mapToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .lastName(account.getLastName())
                .firstName(account.getFirstName())
                .email(account.getEmail())
                .phone(account.getPhone())
                .birthDate(account.getBirthDate())
                .registeredAt(account.getRegisteredAt())
                .build();
    }

}