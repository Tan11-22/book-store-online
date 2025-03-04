package com.BookStore.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    private String deliveryAddress;
    private Integer typePayment;
    private String username;
    private String shippingFee;
    private String recipientPhone;
    private List<CartItemDTO> cartItems;
}
