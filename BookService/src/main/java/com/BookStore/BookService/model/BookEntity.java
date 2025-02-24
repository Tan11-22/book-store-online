package com.BookStore.BookService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SACH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {
    @Id
    @Column(name = "ISBN")
    private String isbn;
    @Column(name = "TENSACH")
    private String tenSach;
    @Column(name="KHUONKHO")
    private String khuonKho;
    @Column(name = "SOTRANG")
    private Integer soTrang;
    @Column(name="TRONGLUONG")
    private Integer trongLuong;
    @Column(name = "MOTA")
    private String moTa;
    @Column(name = "SOLUONG")
    private Integer soLuong;
    @Column(name = "MANHAXUATBAN")
    private String maNhaXuatBan;
}
