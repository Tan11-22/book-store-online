package com.BookStore.AuthenticationService.service.impl;

import com.BookStore.AuthenticationService.model.AccountEntity;
import com.BookStore.AuthenticationService.repository.AccountRepository;
import com.BookStore.AuthenticationService.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void saveRefreshToken(String refreshToken) {
        AccountEntity accountEntity = accountRepository.findByRefreshToken(refreshToken).orElseThrow();
        accountEntity.setRefreshToken(refreshToken);
        accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public AccountEntity getAccountByRefreshToken(String refreshToken) {
        return accountRepository.findByRefreshToken(refreshToken).orElseThrow();
    }
}
