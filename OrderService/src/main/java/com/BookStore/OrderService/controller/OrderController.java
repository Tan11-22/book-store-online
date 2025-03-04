package com.BookStore.OrderService.controller;

import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.OrderRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order-service/order/")
public class OrderController {

    @PostMapping("create")
    public BookStoreResponse createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return null;
    }
}
