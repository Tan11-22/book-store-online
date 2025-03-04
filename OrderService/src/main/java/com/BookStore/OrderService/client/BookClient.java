package com.BookStore.OrderService.client;

import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.modules.bookGrpc.OrderRes;

import java.util.List;

public interface BookClient {
    CartItemDTO getCartItem(CartEntity cartEntity);
    OrderRes checkQuantityBook(List<CartItemDTO> cartItemDTOList);
}
