package com.BookStore.BookService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOverviewDTO {

    private String isbn;

    private String title;

    private String formatSize;

    private Integer pageCount;

    private Integer weight;

    private String description;

    private Integer quantity;

    private String publisherCode;

    private String publisherName;

    private Integer salePrice;

    private Integer discountPrice;

    private Integer reviewCount;

    private Integer totalRating;

}
