package com.krimo.account.service;

import com.krimo.account.model.Account;
import com.krimo.account.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface AccountService {
    Long createAccount(AccountDTO dto);
    void updateAccount(Long id, AccountDTO dto);
    AccountDTO getAccount(Long id);
    List<AccountDTO> getAccounts();
    String getUserEmail (Long id);

    void deleteAccount(Long id);
}

@RequiredArgsConstructor
@Service
@Transactional
class AccountServiceImpl implements AccountService {

    private final com.krimo.account.repository.AccountRepository accountRepository;

    @Override
    public Long createAccount(AccountDTO dto) {
        Account account = Account.create(
                dto.getLastName(),
                dto.getFirstName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getBirthDate()
        );

        return accountRepository.saveAndFlush(account).getId();
    }

    @Override
    public void updateAccount(Long id, AccountDTO dto) {
        Account account = accountRepository.findById(id).orElseThrow();

        if (dto.getEmail() != null) account.setEmail(dto.getEmail());
        if (dto.getPhone() != null) account.setPhone(dto.getPhone());
        if (dto.getLastName() != null) account.setLastName(dto.getLastName());
        if (dto.getFirstName() != null) account.setFirstName(dto.getFirstName());
        if (dto.getBirthDate() != null) account.setBirthDate(dto.getBirthDate());

        accountRepository.save(account);
     }

    @Override
    public AccountDTO getAccount(Long id) {
        return mapToDTO(accountRepository.findById(id).orElseThrow());
    }


    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public String getUserEmail (Long id) {
        return accountRepository.getReferenceById(id).getEmail();
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
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