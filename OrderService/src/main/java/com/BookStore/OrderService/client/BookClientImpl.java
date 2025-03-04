package com.BookStore.OrderService.client;


import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.modules.bookGrpc.BookRequest;
import com.BookStore.modules.bookGrpc.BookResponse;
import com.BookStore.modules.bookGrpc.BookServiceGrpc;
import com.BookStore.modules.bookGrpc.OrderRes;
import com.BookStore.modules.bookGrpc.BookOrderReq;
import  com.BookStore.modules.bookGrpc.OrderReq;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookClientImpl implements BookClient {

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceStub;


    @Override
    public CartItemDTO getCartItem(CartEntity cartEntity) {
        BookRequest bookRequest = BookRequest.newBuilder().setIsbn(cartEntity.getIsbn()).build();
        BookResponse bookResponse = bookServiceStub.getCartDetails(bookRequest);
        return CartItemDTO.builder()
                .cartId(cartEntity.getCartId())
                .isbn(bookResponse.getIsbn())
                .quantity(cartEntity.getQuantity())
                .title(bookResponse.getTitle())
                .salePrice(bookResponse.getSalePrice())
                .discountPrice(bookResponse.getDiscountPrice())
                .image(bookResponse.getImage())
                .pageCount(bookResponse.getPageCount())
                .weight(bookResponse.getWeight())
                .selected(false)
                .build();
    }

    @Override
    public OrderRes checkQuantityBook(List<CartItemDTO> cartItemDTOList) {
        List<BookOrderReq> bookOrderReqList = cartItemDTOList.stream().map(item-> BookOrderReq.newBuilder()
                .setCartId(item.getCartId())
                .setIsbn(item.getIsbn())
                .setQuantity(item.getQuantity())
                .build()).toList();
        OrderReq orderReq = OrderReq.newBuilder()
                .addAllBookItem(bookOrderReqList)
                .build();
        return bookServiceStub.createOrder(orderReq);
    }
}
