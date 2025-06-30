package com.BookStore.AuthenticationService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CUSTOMER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {
    @Id
    private String email;
    private String username;
    private String lastName;
    private String firstName;
    private Boolean gender;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String avatar;
    @OneToOne
    @MapsId
    @JoinColumn(name = "email")
    private AccountEntity account;
}
