package com.BookStore.OrderService.service.impl;

import com.BookStore.OrderService.client.BookClient;
import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.OrderService.repository.CartRepository;
import com.BookStore.OrderService.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookClient bookClient;

    @Override
    public BookStoreResponse getCartItemsByUsername(String username) {
        List<CartEntity> cartEntityList = cartRepository.findByUsername(username);
        List<CartItemDTO> cartItemDTOList = new ArrayList<>();
        for (CartEntity cartEntity : cartEntityList) {
            CartItemDTO item = bookClient.getCartItem(cartEntity);
            cartItemDTOList.add(item);
        }
        return BookStoreResponse.builder()
                .status("cart-details")
                .data(cartItemDTOList)
                .code(200)
                .build();
    }

    @Override
    @Transactional
    public BookStoreResponse addCartItem(CartEntity cartItem) {
        CartEntity item = cartRepository.findCartEntityByUsernameAndIsbn(cartItem.getUsername(), cartItem.getIsbn());

        try {
            if (item != null) {
                System.out.println(item.toString());
                item.setQuantity(item.getQuantity() + 1);
                cartRepository.save(item);
            } else {
                cartRepository.save(cartItem);
            }
            return BookStoreResponse.builder()
                    .status("cart-add-success")
                    .data(null)
                    .code(200)
                    .build();
        } catch (Exception e) {
            System.out.println("ERROR: " +e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BookStoreResponse.builder()
                    .status("server-error")
                    .data(null)
                    .code(500)
                    .build();
        }
    }

    @Override
    @Transactional
    public BookStoreResponse updateQuantityCartItem(CartEntity cartItem) {
        CartEntity item = cartRepository.findById(cartItem.getCartId()).orElse(null);
        if (item == null) return BookStoreResponse.builder()
                .status("book-not-found")
                .data(null)
                .code(404)
                .build();
        try {
                item.setQuantity(cartItem.getQuantity());
                cartRepository.save(item);
            return BookStoreResponse.builder()
                    .status("cart-update-success")
                    .data(null)
                    .code(200)
                    .build();
        }
        catch (Exception e) {
            System.out.println("ERROR: " +e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BookStoreResponse.builder()
                    .status("server-error")
                    .data(null)
                    .code(500)
                    .build();
        }
    }

    @Override
    public BookStoreResponse removeCartItem(int cartId) {
        try {
            cartRepository.deleteById(cartId);
            return BookStoreResponse.builder()
                    .status("cart-remove-success")
                    .data(null)
                    .code(200)
                    .build();
        }
        catch (Exception e) {
            System.out.println("ERROR: " +e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return BookStoreResponse.builder()
                    .status("server-error")
                    .data(null)
                    .code(500)
                    .build();
        }
    }
}
