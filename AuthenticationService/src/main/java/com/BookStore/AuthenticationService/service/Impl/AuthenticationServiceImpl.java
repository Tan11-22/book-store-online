package com.BookStore.AuthenticationService.service.Impl;

import com.BookStore.AuthenticationService.dto.AuthenticationRequest;
import com.BookStore.AuthenticationService.dto.AuthenticationResponse;
import com.BookStore.AuthenticationService.jwt.JwtTokenProvider;
import com.BookStore.AuthenticationService.model.AccountEntity;
import com.BookStore.AuthenticationService.service.AccountService;
import com.BookStore.AuthenticationService.service.AuthenticationService;

import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${app-jwt-access-token-expiration-milliseconds}")
    private long accessTokenExpiration;
    @Value("${app-jwt-refresh-token-expiration-milliseconds}")
    private long refreshTokenExpiration;

    public AuthenticationServiceImpl(AccountService accountService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
            new   UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        AccountEntity accountEntity = accountService.getAccountByEmail(authenticationRequest.getEmail());
        String accessToken = null;
        String refreshToken = null;
        accountService.saveRefreshToken(refreshToken);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public String refreshToken(String refreshToken) {
        String email = jwtTokenProvider.extractEmail(refreshToken);
        return "";
    }

    private String createAccessToken(String email, Map<String, ?> claims) {
        return jwtTokenProvider.generateToken(
                email,
                new Date(System.currentTimeMillis() + accessTokenExpiration),
                claims
        );
    }

    private String createRefreshToken(String email) {
        return jwtTokenProvider.generateToken(
                email,
                new Date(System.currentTimeMillis() + refreshTokenExpiration),
                Collections.emptyMap()
        );
    }

}
