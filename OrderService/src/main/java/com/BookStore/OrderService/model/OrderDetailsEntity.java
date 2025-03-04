package com.BookStore.OrderService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name="isbn")
    private String isbn;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "unit_price")
    private Integer unitPrice;
}
