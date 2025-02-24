package com.BookStore.BookService.service;

import com.BookStore.BookService.dto.*;
import com.BookStore.BookService.model.Category;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookStoreResponse<List<BookItemDTO>> getListBookItems(int start, int size);

    BookStoreResponse<BookInfoDTO> getBookInfo(String isbn);

//    BookStoreResponse<List<BookItemDTO>> timSach(String search, int start, int size);
//    BookStoreResponse demSLSachTimRa(String search);

    BookStoreResponse getBestSellingBooks(int start, int size);

    BookStoreResponse<List<BookItemDTO>> searchBooks(Map<String, Object> data);
    BookStoreResponse<Integer> countSearchResults(Map<String, Object> data);

    BookStoreResponse<List<TacGiaDTO>> layTCTG();
    BookStoreResponse<List<Category>> layTCTL();

    BookStoreResponse<List<BookItemDTO>> getBooksInSameCategory(String isbn);

    BookDto getBookDtoByIsbn(String isbn);
}
