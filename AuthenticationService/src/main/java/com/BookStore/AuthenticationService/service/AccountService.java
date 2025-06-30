package com.BookStore.AuthenticationService.service;

import com.BookStore.AuthenticationService.model.AccountEntity;

public interface AccountService {
    void saveRefreshToken(String refreshToken);
    AccountEntity getAccountByEmail(String email);
}
