package com.jatin.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jatin.orders.client.ProductClient;
import com.jatin.orders.dto.ProductResponse;
import com.jatin.orders.model.Order;
import com.jatin.orders.model.OrderStatus;
import com.jatin.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrdersByUserId(Long id) {
        return orderRepository.findByUserId(id);
    }

    public Order createOrder(Long userId, Order order) {
        order.setStatus(OrderStatus.PENDING);
        List<Long> productIds = order.getProductIds();
        List<ProductResponse> products = productClient.getProductByIds(productIds);
        BigDecimal totalPrice = products
                .stream()
                .map(ProductResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setUserId(userId);
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setUserId(orderDetails.getUserId());
            order.setProductIds(orderDetails.getProductIds());
            order.setTotalPrice(orderDetails.getTotalPrice());
            order.setStatus(orderDetails.getStatus());
            order.setOrderDate(orderDetails.getOrderDate());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public Order makePayment(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}
