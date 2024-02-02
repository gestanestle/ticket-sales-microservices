package com.krimo.account.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account", indexes = @Index(name = "idx_user_email", columnList = "email"))
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    private String email;
    private String phone;
    @Column(name = "birthdate")
    private LocalDate birthDate;
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    public Account (String lastName, String firstName, String email, String phone, LocalDate birthDate, LocalDateTime registeredAt) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.registeredAt = registeredAt;
    }

    public static Account create(String lastName, String firstName, String email, String phone, LocalDate birthDate)  {
        return new Account(lastName, firstName, email, phone, birthDate,
                                    LocalDateTime.now());
    }
}
