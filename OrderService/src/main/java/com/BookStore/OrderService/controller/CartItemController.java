package com.BookStore.OrderService.controller;

import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.OrderService.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-service/cart")
public class CartItemController {
    @Autowired
    private CartService cartService;


    @PostMapping("add")
    public BookStoreResponse addCartItem(@RequestBody CartEntity cartEntity) {
        return cartService.addCartItem(cartEntity);
    }

    @PostMapping("update")
    public BookStoreResponse updateCartItem(@RequestBody CartEntity cartEntity) {
        return cartService.updateQuantityCartItem(cartEntity);
    }

    @PostMapping("remove")
    public BookStoreResponse removeCartItem(@RequestBody CartEntity cartEntity) {
        return cartService.removeCartItem(cartEntity.getCartId());
    }
}
