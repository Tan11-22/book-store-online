package com.BookStore.BookService.service.grpc;


import com.BookStore.BookService.dto.BookDto;


import com.BookStore.BookService.model.BookEntity;
import com.BookStore.BookService.repository.BookRepository;
import com.BookStore.BookService.service.BookService;
import com.BookStore.enums.CommonStatus;
import com.BookStore.modules.bookGrpc.BookRequest;
import com.BookStore.modules.bookGrpc.BookResponse;
import com.BookStore.modules.bookGrpc.BookServiceGrpc;
import com.BookStore.modules.bookGrpc.OrderReq;
import com.BookStore.modules.bookGrpc.BookOrderReq;
import com.BookStore.modules.bookGrpc.OrderRes;
import io.grpc.stub.StreamObserver;


import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@GrpcService
public class BookGRPCServiceImpl extends BookServiceGrpc.BookServiceImplBase {


    private final BookService bookService;
    private final BookRepository bookRepository;

    @Autowired
    public BookGRPCServiceImpl(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

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

    @Override
    @Transactional
    public void createOrder(OrderReq request, StreamObserver<OrderRes> responseObserver) {
//        super.createOrder(request, responseObserver);
        CommonStatus status = CommonStatus.FAILED;
        List<BookOrderReq> bookRes = request.getBookItemList().stream()
                .map(item -> { // Kiem tra số lượng sách hiện còn
                    BookEntity book = bookService.getBookEntityByIsbn(item.getIsbn());
                    if (book.getQuantity() < item.getQuantity()) {
                        return BookOrderReq.newBuilder()
                                .setCartId(item.getCartId())
                                .setIsbn(item.getIsbn())
                                .setQuantity(book.getQuantity())
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull) // Loại bỏ các phần tử null
                .collect(Collectors.toList());
        if (bookRes.size() == 0) {
            System.out.println("check đủ số lượng bđ update:");
            try {
                request.getBookItemList().stream().forEach(item -> {
                    BookEntity book = bookService.getBookEntityByIsbn(item.getIsbn());
                    book.setQuantity(book.getQuantity() - item.getQuantity());
                    // test
                    System.out.println(book.toString());
//                    bookRepository.save(book);
                });
                status = CommonStatus.SUCCESS;
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                status = CommonStatus.FAILED;
            }
        } else {
            status = CommonStatus.OUT_OF_STOCK;
        }
        OrderRes orderRes = OrderRes.newBuilder().setCode(status.ordinal()).addAllBookItem(bookRes).build();
        responseObserver.onNext(orderRes);
        responseObserver.onCompleted();
    }





}
