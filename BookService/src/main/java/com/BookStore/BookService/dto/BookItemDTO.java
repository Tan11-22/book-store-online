package com.BookStore.BookService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemDTO {
    private String isbn;
    private String title;
    private String author;
    private Integer salePrice;
    private Integer discountPrice;
    private String image;
    private Integer inventoryQuantity;
}
