package com.BookStore.OrderService.client;

import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;

public interface BookClient {
    CartItemDTO getCartItem(CartEntity cartEntity);
}
