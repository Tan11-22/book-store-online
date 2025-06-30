package com.BookStore.AuthenticationService.service.Impl;

import com.BookStore.AuthenticationService.service.TaiKhoanService;
import com.BookStore.AuthenticationService.dto.BookStoreResponse;
import com.BookStore.AuthenticationService.dto.TaiKhoanDTO;
import com.BookStore.AuthenticationService.dto.UserInfoDTO;
import com.BookStore.AuthenticationService.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    EmailService emailService;

    @Override
    public BookStoreResponse<Boolean> dangKyTaiKhoan(String tenDangNhap, String matKhau, String email, String ho, String ten, String gioiTinh, String diaChi, Date ngaySinh, String soDienThoai) {
        if (taiKhoanRepository.checkTDN(tenDangNhap) == 1) {
            return BookStoreResponse.<Boolean>builder()
                    .code(201)
                    .status("Tên đăng nhập đã tồn tại!")
                    .data(false)
                    .build();
        }

        if (taiKhoanRepository.checkEmail(email) == 1) {
            return BookStoreResponse.<Boolean>builder()
                    .code(202)
                    .status("Email đã tồn tại!")
                    .data(false)
                    .build();
        }
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(matKhau);
            taiKhoanRepository.taoTaiKhoan(tenDangNhap, password, 2);
            taiKhoanRepository.taoKhachHang(tenDangNhap, email, ho, ten, gioiTinh.equals(0) ? false : true, diaChi, ngaySinh, soDienThoai);
            return BookStoreResponse.<Boolean>builder()
                    .code(200)
                    .status("Đăng ký tài khoản thành công!")
                    .data(true)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
            return BookStoreResponse.<Boolean>builder()
                    .code(203)
                    .status("Đã có lỗi xảy ra!")
                    .data(false)
                    .build();
        }
    }

//    @Override
//    public String lauQuyenCuaUser(String tenDangNhap) {
//        return taiKhoanRepository.layQuyenCuaTK(tenDangNhap);
//    }

    @Override
    public BookStoreResponse<Boolean> thayDoiMatKhau(String username, String password, String newpassword) {
        Map<String, Object> user = taiKhoanRepository.loadUser(username);
        PasswordEncoder encode = new BCryptPasswordEncoder();
        if (!(encode.matches(password, (String) user.get("MATKHAU")))) {
            return BookStoreResponse.<Boolean>builder()
                    .code(201)
                    .status("Mật khẩu cũ không chính xác không chính xác!")
                    .data(false).build();
        }
        try {
//            PasswordEncoder encode = new BCryptPasswordEncoder();
            String password1 = encode.encode(newpassword);
            taiKhoanRepository.doiMatKhau(username, password1);
            return BookStoreResponse.<Boolean>builder()
                    .code(200)
                    .status("Cập nhật mật khẩu thành công!")
                    .data(true)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
            return BookStoreResponse.<Boolean>builder()
                    .code(203)
                    .status("Đã có lỗi xảy ra!")
                    .data(false)
                    .build();
        }
    }

    @Override
    @Transactional
    public BookStoreResponse quenMatKhau(String email, String username) {
        Map<String, Object> user = taiKhoanRepository.loadUser(username);
        if (user.get("TENDANGNHAP") == null) {
            return BookStoreResponse.<Boolean>builder()
                    .code(201)
                    .status("Tài khoản không tồn tại!")
                    .data(false).build();
        }
        String email1 = getEmail(user);
        if (email1.equals(email)) {
            try {
                String resetPassword = emailService.generateCode();
                PasswordEncoder encode = new BCryptPasswordEncoder();
                String password1 = encode.encode(resetPassword);
                taiKhoanRepository.doiMatKhau(username, password1);
                System.out.println(resetPassword);
                emailService.sendEmail(email1, "Mật khẩu mới: " + resetPassword);
                return BookStoreResponse.<Boolean>builder()
                        .code(200)
                        .status("Mật khẩu mới đã được gửi đến email của bạn!")
                        .data(true)
                        .build();
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
                return BookStoreResponse.<Boolean>builder()
                        .code(203)
                        .status("Đã có lỗi xảy ra!")
                        .data(false)
                        .build();
            }
        }
        return BookStoreResponse.<Boolean>builder()
                .code(202)
                .status("Địa chỉ email không chính xác!")
                .data(false).build();
    }

    @Override
    public UserInfoDTO layThongTinUser(String tenDangNhap, String token) {
        Map<String, Object> userDefault = taiKhoanRepository.layThongTinUser(tenDangNhap);

        return UserInfoDTO.builder()
                .username((String) userDefault.get("TENDANGNHAP"))
                .role((String) userDefault.get("QUYEN"))
                .token(token)
                .hoTen((String) userDefault.get("HOTEN"))
                .hinhAnh((String) userDefault.get("HINHANH"))
                .build();
    }

    public String getEmail(Map<String, Object> user) {

        if (((String) user.get("TENQUYEN")).equals("KHACHHANG")) {
            return taiKhoanRepository.getEmailKH((String) user.get("TENDANGNHAP"));
        } else {
            return taiKhoanRepository.getEmailNV((String) user.get("TENDANGNHAP"));
        }
    }

    @Override
    public BookStoreResponse<List<TaiKhoanDTO>> getDanhSachTaiKhoan(int type) {
        List<Map<String,Object>> data = type == 0 ? taiKhoanRepository.getTaiKhoanNV():taiKhoanRepository.getTaiKhoanKH();
        List<TaiKhoanDTO> result = data.stream().map(map->mapToTaiKhoanDTO(map)).toList();
        return BookStoreResponse.<List<TaiKhoanDTO>>builder()
                .code(200)
                .status("")
                .data(result).build();
    }

    private TaiKhoanDTO mapToTaiKhoanDTO(Map<String, Object> map) {
        return TaiKhoanDTO.builder()
                .tenDangNhap((String) map.get("TENDANGNHAP"))
                .trangThai((Boolean) map.get("TRANGTHAI"))
                .ho((String) map.get("HO"))
                .ten((String) map.get("TEN"))
                .email((String) map.get("EMAIL"))
                .tenQuyen((String) map.get("TENQUYEN"))
                .build();
    }

    @Override
    public BookStoreResponse<Boolean>capNhatTrangThaiTK(String tenDangNhap, Boolean tt) {
        try {
//            PasswordEncoder encode = new BCryptPasswordEncoder();

            taiKhoanRepository.capNhatTrangThai(tenDangNhap, tt);
            return BookStoreResponse.<Boolean>builder()
                    .code(200)
                    .status("Cập nhật trang thai thành công!")
                    .data(true)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
            return BookStoreResponse.<Boolean>builder()
                    .code(203)
                    .status("Đã có lỗi xảy ra!")
                    .data(false)
                    .build();
        }

    }

    @Override
    public BookStoreResponse<Boolean> taoTaiKhoanNV(
        Map<String, Object> data
    ) {
        String tenDangNhap = (String) data.get("tenDangNhap");
        String email = (String) data.get("email");
        String ho = (String) data.get("ho");
        String ten = (String) data.get("ten");
        String gioiTinh = (String) data.get("gioiTinh");
        String diaChi= (String) data.get("diaChi");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date ngaySinh = null;
        try {
            ngaySinh = dateFormat.parse((String) data.get("ngaySinh"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String soDienThoai = (String) data.get("soDienThoai");
        Integer idQuyen = (Integer) data.get("idQuyen");
        System.out.println(idQuyen);
        if (taiKhoanRepository.checkTDN(tenDangNhap) == 1) {
            return BookStoreResponse.<Boolean>builder()
                    .code(201)
                    .status("Tên đăng nhập đã tồn tại!")
                    .data(false)
                    .build();
        }

        if (taiKhoanRepository.checkEmail(email) == 1) {
            return BookStoreResponse.<Boolean>builder()
                    .code(202)
                    .status("Email đã tồn tại!")
                    .data(false)
                    .build();
        }
        try {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode("123456");
            taiKhoanRepository.taoTaiKhoan(tenDangNhap, password, idQuyen);
            taiKhoanRepository.taoNhanVien(tenDangNhap, email, ho, ten, gioiTinh.equals(0) ? false : true, diaChi, ngaySinh, soDienThoai);
            return BookStoreResponse.<Boolean>builder()
                    .code(200)
                    .status("Tạo tài khoản thành công!")
                    .data(true)
                    .build();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
            return BookStoreResponse.<Boolean>builder()
                    .code(203)
                    .status("Đã có lỗi xảy ra!")
                    .data(false)
                    .build();
        }
    }
}
