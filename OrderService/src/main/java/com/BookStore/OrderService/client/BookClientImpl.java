package com.BookStore.OrderService.client;


import com.BookStore.OrderService.dto.CartItemDTO;
import com.BookStore.OrderService.model.CartEntity;
import com.BookStore.modules.bookGrpc.BookRequest;
import com.BookStore.modules.bookGrpc.BookResponse;
import com.BookStore.modules.bookGrpc.BookServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

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
}
