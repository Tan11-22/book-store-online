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
    private String email;
    private String employeeName;
    private String lastName;
    private String firstName;
    private Boolean gender;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String avatar;
    private String socialInsuranceNumber;
    @OneToOne
    @MapsId
    @JoinColumn(name = "email")
    private AccountEntity account;
}
