package com.krimo.userprofile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    private Long id;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;
    private String email;
    private String phone;
    @JsonProperty("birthdate")
    private LocalDate birthDate;
    @JsonProperty("registered_at")
    private LocalDateTime registeredAt;
}
