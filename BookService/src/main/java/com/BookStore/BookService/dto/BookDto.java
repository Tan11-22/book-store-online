package com.BookStore.BookService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String isbn;
    private String title;
    private Integer salePrice;
    private Integer discountPrice;
    private String image;
    private Integer pageCount;
    private Integer weight;
    private Integer inventoryQuantity;
}
