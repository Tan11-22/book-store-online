package com.BookStore.OrderService.service;

import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.OrderRequestDTO;

public interface OrdersService {
    BookStoreResponse createOrder(OrderRequestDTO order);
}
