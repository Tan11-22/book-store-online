package com.BookStore.AuthenticationService.service.impl;

import com.BookStore.AuthenticationService.dto.AuthenticationRequest;
import com.BookStore.AuthenticationService.dto.AuthenticationResponse;
import com.BookStore.AuthenticationService.jwt.JwtTokenProvider;
import com.BookStore.AuthenticationService.model.AccountEntity;
import com.BookStore.AuthenticationService.service.AccountService;
import com.BookStore.AuthenticationService.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${app-jwt-access-token-expiration-milliseconds}")
    private long accessTokenExpiration;
    @Value("${app-jwt-refresh-token-expiration-milliseconds}")
    private long refreshTokenExpiration;

    public AuthenticationServiceImpl(UserDetailsService userDetailsService, AccountService accountService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);
        accountService.saveRefreshToken(refreshToken);
        return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public String refreshToken(String refreshToken) {
        String email = jwtTokenProvider.extractEmail(refreshToken);
        UserDetails currentUserDetails = userDetailsService.loadUserByUsername(email);
        AccountEntity refreshTokenUserDetails = accountService.getAccountByRefreshToken(refreshToken);
        if (currentUserDetails.getUsername().equals(refreshTokenUserDetails.getEmail())) {
            return createAccessToken(currentUserDetails);
        } else throw new AuthenticationServiceException("Invalid refresh token");
    }

    private String createAccessToken(UserDetails user) {
        return jwtTokenProvider.generateToken(
                user.getUsername(),
                new Date(System.currentTimeMillis() + accessTokenExpiration),
                createCustomClaims(user)
        );
    }

    private String createRefreshToken(UserDetails user) {
        return jwtTokenProvider.generateToken(
                user.getUsername(),
                new Date(System.currentTimeMillis() + refreshTokenExpiration),
                createCustomClaims(user)
        );
    }

    private Map<String, ?> createCustomClaims(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
//        claims.put("email", user.getUsername());
        claims.put("roleName", user.getAuthorities());
        return claims;
    }

}
