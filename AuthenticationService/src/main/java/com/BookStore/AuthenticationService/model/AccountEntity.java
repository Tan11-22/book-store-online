package com.BookStore.AuthenticationService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {
    @Id
    private String email;
    @Column(nullable = true)
    private String password;
    private Boolean status;
    @Column(unique = true, nullable = true) // Google ID must be unique if it exists
    private String googleId;
    private boolean verified = false;
    @Column(columnDefinition = "TEXT", nullable = true)
    private String refreshToken;

    @Column(nullable = true)
    private LocalDateTime refreshTokenExpiry;
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerEntity customer;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmployeeEntity employee;
}

