package com.krimo.account.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private LocalDateTime registeredAt;
}
