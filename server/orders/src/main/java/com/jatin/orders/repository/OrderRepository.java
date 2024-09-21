package com.jatin.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.orders.model.Order;

/**
 * OrderRepository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
}
