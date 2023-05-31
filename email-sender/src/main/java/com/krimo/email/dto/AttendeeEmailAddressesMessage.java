package com.krimo.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendeeEmailAddressesMessage {
    private String eventCode;
    private String emailAddress;
}
