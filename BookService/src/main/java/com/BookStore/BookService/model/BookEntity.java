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
    private String title;
    @Column(name="KHUONKHO")
    private String sizeFormat;

    @Column(name = "SOTRANG")
    private Integer pageNumber;

    @Column(name="TRONGLUONG")
    private Integer weight;

    @Column(name = "MOTA")
    private String description;

    @Column(name = "SOLUONG")
    private Integer quantity;

    @Column(name = "MANHAXUATBAN")
    private String publisherCode;
}
