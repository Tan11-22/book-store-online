package com.BookStore.OrderService.repository;

import com.BookStore.OrderService.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    List<CartEntity> findByUsername(String username);
    CartEntity findCartEntityByUsernameAndIsbn(String username, String isbn);
}
