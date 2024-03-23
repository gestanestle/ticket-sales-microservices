package com.krimo.account.data;

import com.krimo.account.dto.AccountDTO;
import com.krimo.account.model.Account;
import org.springframework.cache.Cache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class AccountFactory {

    private static final String LASTNAME = "Swift";
    private static final String FIRSTNAME = "Taylor";
    private static final String EMAIL = "email@gmail.com";
    private static final String PHONE = "+63123456789";
    private static final LocalDate BIRTHDAY = LocalDate.of(1989, Month.DECEMBER, 13);

    public static Account accountInit() {
        return Account.create(LASTNAME, FIRSTNAME, EMAIL, PHONE, BIRTHDAY);
    }

    public static AccountDTO dtoInit() {
        return new AccountDTO(1L, LASTNAME, FIRSTNAME, EMAIL, PHONE, BIRTHDAY, LocalDateTime.now());
    }

    public static AccountDTO updateDtoInit() {
        return new AccountDTO(1L, "Sheesh", FIRSTNAME, EMAIL, PHONE, BIRTHDAY, LocalDateTime.now());
    }

}
