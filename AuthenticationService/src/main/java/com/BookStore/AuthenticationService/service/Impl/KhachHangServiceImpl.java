package com.BookStore.AuthenticationService.service.Impl;

import com.BookStore.AuthenticationService.service.KhachHangService;
import com.BookStore.AuthenticationService.dto.BookStoreResponse;
import com.BookStore.AuthenticationService.model.KhachHang;
import com.BookStore.AuthenticationService.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KhachHangServiceImpl implements KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;


    @Override
    public BookStoreResponse<KhachHang> getInfoKhachHang(String tenDangNhap) {
        KhachHang result = khachHangRepository.findById(tenDangNhap).get();
        return BookStoreResponse.<KhachHang>builder()
                .code(200)
                .status("Lấy thông tin khách hàng thành công!")
                .data(result).build();
    }
}
