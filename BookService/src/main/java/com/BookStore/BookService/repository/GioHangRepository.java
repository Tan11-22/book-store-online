package com.BookStore.BookService.repository;

import com.BookStore.BookService.model.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    @Procedure(procedureName = "SP_THEM_SACH_VAO_GIO_HANG")
    void themSachVaoGioHang(
            @Param("tendangnhap") String tenDangNhap,
            @Param("isbn") String isbn,
            @Param("soluong") Integer soLuong
    );

    @Procedure(procedureName = "SP_CAP_NHAT_SACH_TRONG_GIO_HANG")
    void capNhatSachTrongGioHang(
            @Param("id") Integer id,
            @Param("soluong") Integer soLuong
    );

    @Procedure(procedureName = "SP_XOA_SACH_TRONG_GIO_HANG")
    void xoaSachTrongGioHang(
            @Param("id") Integer id
    );

    @Query(value = "{call SP_LAY_CHI_TIET_GIO_HANG(:tendangnhap)}", nativeQuery = true)
    List<Map<String,Object>> layChiTietGioHang(@Param("tendangnhap") String tenDangNhap);

    @Query(value = "{call SP_LAY_ID_GIO_HANG(:tendangnhap, :isbn)}", nativeQuery = true)
    Map<String,Object> layIdGioHang(@Param("tendangnhap") String tenDangNhap,
                         @Param("isbn") String isbn);


    @Query(value = "{call SP_LAY_SL_SACH_GH(:tendangnhap)}", nativeQuery = true)
    Integer laySLSachTrongGH(@Param("tendangnhap") String tenDangNhap);

    @Query(value = "SELECT SODIENTHOAI FROM KHACHHANG WHERE TENDANGNHAP=:tdn", nativeQuery = true)
    String getSoDienThoai(@Param("tdn") String tdn);


}
