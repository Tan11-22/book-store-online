package com.BookStore.OrderService.repository;

import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Integer, Order> {
}
