package com.BookStore.AuthenticationService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMPLOYEE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {
    @Id
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "GENDER")
    private Boolean gender;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "SOCIAL_INSURANCE_NUMBER")
    private String socialInsuranceNumber;

    @OneToOne
    @MapsId
    @JoinColumn(name = "EMAIL")
    private AccountEntity account;
}