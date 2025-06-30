package com.BookStore.AuthenticationService.repository;

import com.BookStore.AuthenticationService.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByRefreshToken(String refreshToken);
    Optional<AccountEntity> findByEmail(String email);
}
