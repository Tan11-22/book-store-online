package com.BookStore.AuthenticationService.service;


import com.BookStore.AuthenticationService.dto.AuthenticationRequest;
import com.BookStore.AuthenticationService.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    String refreshToken(String refreshToken);
}
