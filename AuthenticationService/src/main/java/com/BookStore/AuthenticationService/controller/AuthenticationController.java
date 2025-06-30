package com.BookStore.AuthenticationService.controller;

import com.BookStore.AuthenticationService.service.*;
import com.BookStore.AuthenticationService.dto.BookStoreResponse;
import com.BookStore.AuthenticationService.dto.UserInfoDTO;
import com.BookStore.AuthenticationService.jwt.JwtTokenProvider;
import com.BookStore.AuthenticationService.model.KhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/authentication-service/")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private NhanVienService nhanVienService;


    @Autowired
    private GoogleService googleService;

    @PostMapping("login")
    public UserInfoDTO testLogin(@RequestBody Map<String, String> user) {
//        System.out.println("us: "+ user.get("tendangnhap"));
//        System.out.println("pass: "+ user.get("matkhau"));
        String token = authService.login(user.get("tendangnhap"), user.get("matkhau"));
//        String role = taiKhoanService.getRoleUser(user.get("username"));

        return taiKhoanService.layThongTinUser(user.get("tendangnhap"), token);
    }


    @PostMapping("dang-ky")
    public BookStoreResponse<Boolean> registerUser(
            @RequestParam("tenDangNhap") String tenDangNhap,
            @RequestParam("matKhau") String matKhau,
            @RequestParam("email") String email,
            @RequestParam("ho") String ho,
            @RequestParam("ten") String ten,
            @RequestParam("gioiTinh") String gioiTinh,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("ngaySinh") @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngaySinh,
            @RequestParam("soDienThoai") String soDienThoai
    ) {
        return taiKhoanService.dangKyTaiKhoan(tenDangNhap, matKhau, email, ho, ten, gioiTinh, diaChi, ngaySinh, soDienThoai);
    }

    @GetMapping("get-info")
    public BookStoreResponse<KhachHang> getInfoKhachHang(@RequestParam("tdn") String tenDangNhap) {
        System.out.println(tenDangNhap);
        return khachHangService.getInfoKhachHang(tenDangNhap);
    }

    @GetMapping("get-info-nv")
    public BookStoreResponse<KhachHang> getInfoNhanVien(@RequestParam("tdn") String tenDangNhap) {
        return nhanVienService.getInfoNhanVien(tenDangNhap);
    }

    @PostMapping("thay-doi-mat-khau")
    public BookStoreResponse<Boolean> thayDoiMatKhau(@RequestBody Map<String, String> userInfo) {
        System.out.println(userInfo);
        return taiKhoanService.thayDoiMatKhau(userInfo.get("username"), userInfo.get("password"),
                userInfo.get("newpassword"));
    }

    @PostMapping("valid")
    public Map<String, Boolean> valid(@RequestBody Map<String, String> token) {
        System.out.println("check valid token");
        Boolean isValid = jwtTokenProvider.validateToken(token.get("token"));
//        System.out.println(isValid);
//        System.out.println("_-_-_-_-_");
        Map<String, Boolean> map = new HashMap<>();
        map.put("valid", isValid);
        return map;
    }

    @GetMapping("quen-mat-khau")
    public BookStoreResponse quenMatKhau(@RequestParam("username") String username,
                                         @RequestParam("email") String email) {
        return taiKhoanService.quenMatKhau(email, username);
    }

    @GetMapping("login-google")
    public BookStoreResponse generateURL() {
        return googleService.generateURL();
    }

    @PostMapping("login-google-callback")
    public BookStoreResponse loginGoogleCallback(@RequestBody Map<String, String> data) {
        return googleService.loginGoogle(data);
    }

    @GetMapping("ds-tai-khoan")
    public BookStoreResponse getDanhSachTaiKhoan(@RequestParam("type") int type) {
        return taiKhoanService.getDanhSachTaiKhoan(type);
    }

    @PostMapping("cap-nhat-tk")
    public BookStoreResponse capNhatTK(@RequestBody Map<String, Object> data) {
        return taiKhoanService.capNhatTrangThaiTK((String) data.get("tdn"), (Boolean) data.get("tt"));
    }

    @PostMapping("tao-tai-khoan-nv")
    public BookStoreResponse<Boolean> taoTaiKhoanNv(@RequestBody Map<String, Object> data
    ) {
        return taiKhoanService.taoTaiKhoanNV(data);
    }
}
