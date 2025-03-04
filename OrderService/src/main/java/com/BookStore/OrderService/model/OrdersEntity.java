package com.BookStore.OrderService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name="username")
    private String username;
    @Column(name = "purchase_date")
    private String purchaseDate;
    @Column(name = "payment_status")
    private Integer paymentStatus;
    @Column(name = "payment_date")
    private String paymentDate;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "shipping_fee")
    private Integer shippingFee;
    @Column(name = "order_status")
    private Integer orderStatus;
    @Column(name = "recipient_phone")
    private String recipientPhone;
    @Column(name = "approved_by")
    private String approvedBy;
    @Column(name = "delivered_by")
    private String deliveredBy;

}
