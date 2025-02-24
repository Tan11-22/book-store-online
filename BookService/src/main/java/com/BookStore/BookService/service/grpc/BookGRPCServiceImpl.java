package com.BookStore.BookService.service.grpc;


import com.BookStore.BookService.dto.BookDto;


import com.BookStore.BookService.service.BookService;
import com.BookStore.modules.bookGrpc.BookRequest;
import com.BookStore.modules.bookGrpc.BookResponse;
import com.BookStore.modules.bookGrpc.BookServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


@AllArgsConstructor
@GrpcService
public class BookGRPCServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    @Autowired
    private BookService bookService;


    @Override
    public void getCartDetails(BookRequest request, StreamObserver<BookResponse> responseObserver) {
//        super.getCartDetails(request, responseObserver);
        BookDto book = bookService.getBookDtoByIsbn(request.getIsbn());
        System.out.println(book.toString());
        BookResponse bookResponse = BookResponse.newBuilder()
                .setIsbn(book.getIsbn())
                .setTitle(book.getTitle())
                .setSalePrice(book.getSalePrice() != null ? book.getSalePrice() : -1)
                .setDiscountPrice(book.getDiscountPrice() != null ? book.getDiscountPrice() : -1)
                .setImage(book.getImage())
                .setPageCount(book.getPageCount())
                .setWeight(book.getWeight()).build();
        responseObserver.onNext(bookResponse);
        responseObserver.onCompleted();
    }

}
