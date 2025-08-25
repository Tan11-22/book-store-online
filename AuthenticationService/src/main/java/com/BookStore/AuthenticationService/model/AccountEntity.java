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
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD", nullable = true)
    private String password;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "GOOGLE_ID", unique = true, nullable = true)
    private String googleId;

    @Column(name = "VERIFIED")
    private boolean verified = false;

    @Column(name = "REFRESH_TOKEN", columnDefinition = "TEXT", nullable = true)
    private String refreshToken;

    @Column(name = "REFRESH_TOKEN_EXPIRY", nullable = true)
    private LocalDateTime refreshTokenExpiry;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER fetch is often useful for roles
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CustomerEntity customer;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmployeeEntity employee;
}