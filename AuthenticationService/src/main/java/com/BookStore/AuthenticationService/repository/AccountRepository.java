package com.BookStore.AuthenticationService.repository;

import com.BookStore.AuthenticationService.dto.User;
import com.BookStore.AuthenticationService.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByRefreshToken(String refreshToken);
    Optional<AccountEntity> findByEmail(String email);



    @Query(value = """
        SELECT a.email, a.password, r.role_name FROM 
            (SELECT email, password, role_id FROM ACCOUNT WHERE email = :email) AS a
        INNER JOIN (SELECT * FROM ROLE) r ON a.role_id = r.role_id
    """, nativeQuery = true)
    User findUserByEmail(@Param("email") String email);
}
