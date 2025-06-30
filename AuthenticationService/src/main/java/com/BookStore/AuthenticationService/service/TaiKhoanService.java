package com.BookStore.AuthenticationService.service;

import com.BookStore.AuthenticationService.dto.BookStoreResponse;
import com.BookStore.AuthenticationService.dto.UserInfoDTO;

import java.util.Date;
import java.util.Map;

public interface TaiKhoanService {
    BookStoreResponse<Boolean> dangKyTaiKhoan(String tenDangNhap,
                                              String matKhau,
                                              String email,
                                              String ho,
                                              String ten,
                                              String gioiTinh,
                                              String diaChi,
                                              Date ngaySinh,
                                              String soDienThoai);


    BookStoreResponse<Boolean> thayDoiMatKhau(String username, String password, String newpassword);

    BookStoreResponse quenMatKhau(String email, String username);

    UserInfoDTO layThongTinUser(String tenDangNhap, String token);

    BookStoreResponse getDanhSachTaiKhoan(int type);

    BookStoreResponse capNhatTrangThaiTK(String tenDangNhap, Boolean tt);

    BookStoreResponse<Boolean> taoTaiKhoanNV(Map<String, Object> data);
}
