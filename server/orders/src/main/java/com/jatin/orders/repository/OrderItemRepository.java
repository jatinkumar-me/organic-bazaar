package com.jatin.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.orders.model.OrderItem;

/**
 * OrderItemRepository
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
