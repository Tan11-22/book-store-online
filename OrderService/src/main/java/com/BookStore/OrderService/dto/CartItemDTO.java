package com.BookStore.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Integer cartId;
    private String isbn;
    private Integer quantity;
    private String title;
    private Integer salePrice;
    private Integer discountPrice;
    private String image;
    private Integer pageCount;
    private Integer weight;
//    private int inventoryQuantity;
    private boolean selected;
}
