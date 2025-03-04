package com.BookStore.OrderService.service;

import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;

import java.util.List;

public interface CartService {
    BookStoreResponse getCartItemsByUsername(String username);
    BookStoreResponse getTotalItemsByUsername(String username);
    BookStoreResponse addCartItem(CartEntity cartItem);
    BookStoreResponse updateQuantityCartItem(CartEntity cartItem);
    BookStoreResponse removeCartItem(int cartItem);
}
