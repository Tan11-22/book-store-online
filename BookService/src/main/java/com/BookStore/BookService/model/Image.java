package com.BookStore.BookService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HINHANH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDANH")
    private Integer idAnh;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "FILENAME")
    private String filename;

}
