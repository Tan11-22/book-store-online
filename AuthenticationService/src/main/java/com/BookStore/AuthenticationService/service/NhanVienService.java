package com.BookStore.AuthenticationService.service;

import com.BookStore.AuthenticationService.dto.BookStoreResponse;

public interface NhanVienService {
    BookStoreResponse getInfoNhanVien(String tenDangNhap);
}
