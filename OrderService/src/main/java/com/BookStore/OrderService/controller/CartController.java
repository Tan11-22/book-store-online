package com.BookStore.OrderService.controller;

import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.OrderService.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-service/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("details")
    public BookStoreResponse getCartDetailsByUsername(@RequestParam("username") String username) {
        return cartService.getCartItemsByUsername(username);
    }

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
