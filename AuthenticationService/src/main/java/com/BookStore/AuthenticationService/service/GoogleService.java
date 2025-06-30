package com.BookStore.AuthenticationService.service;

import com.BookStore.AuthenticationService.dto.BookStoreResponse;

import java.util.Map;

public interface GoogleService {
    BookStoreResponse generateURL();
    BookStoreResponse loginGoogle(Map<String, String> data);
}
