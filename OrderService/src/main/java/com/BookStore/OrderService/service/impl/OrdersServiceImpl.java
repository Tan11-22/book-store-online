package com.BookStore.OrderService.service.impl;

import com.BookStore.OrderService.client.BookClient;
import com.BookStore.OrderService.dto.BookStoreResponse;
import com.BookStore.OrderService.dto.OrderRequestDTO;
import com.BookStore.OrderService.repository.OrdersRepository;
import com.BookStore.OrderService.service.OrdersService;
import com.BookStore.enums.CommonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.BookStore.modules.bookGrpc.OrderRes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.util.HashMap;
import java.util.Map;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private BookClient bookClient;

    @Autowired
    public OrdersServiceImpl(OrdersRepository ordersRepository
    , BookClient bookClient) {
        this.ordersRepository = ordersRepository;
        this.bookClient = bookClient;
    }

    @Override
    public BookStoreResponse createOrder(OrderRequestDTO order) {
        // kiểm tra sách hiện còn
        OrderRes orderRes = bookClient.checkQuantityBook(order.getCartItems());
        if(orderRes.getCode() == CommonStatus.FAILED.ordinal()) {
            return BookStoreResponse.builder()
                    .code(CommonStatus.FAILED.ordinal())
                    .data(null)
                    .status("")
                    .build();
        } else if(orderRes.getCode() == CommonStatus.OUT_OF_STOCK.ordinal()) {
            return BookStoreResponse.builder()
                    .code(CommonStatus.OUT_OF_STOCK.ordinal())
                    .data(orderRes.getBookItemList())
                    .status("")
                    .build();
        } else {
            // Trừ số lượng sách thành công
            // Tạo order -- insert order --> insert order detail --> remove book in cart

            // tạo payment nếu thanh toán online
        }

        return null;
    }



    @Transactional
    void insertOrder(OrderRequestDTO order) {
        try {

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}
