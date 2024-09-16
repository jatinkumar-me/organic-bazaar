package com.jatin.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jatin.orders.client.ProductClient;
import com.jatin.orders.dto.OrderDTO;
import com.jatin.orders.dto.OrderItemDTO;
import com.jatin.orders.dto.ProductResponse;
import com.jatin.orders.model.Order;
import com.jatin.orders.model.OrderItem;
import com.jatin.orders.model.OrderStatus;
import com.jatin.orders.repository.OrderItemRepository;
import com.jatin.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductClient productClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Optional<Order> getOrdersByUserId(Long id) {
        return orderRepository.findByUserId(id);
    }

    public Order createOrder(Long userId, OrderDTO orderDTO) {
        Order order = new Order();

        List<Long> productIds = orderDTO.getOrderItems()
                .stream()
                .map(OrderItemDTO::getProductId)
                .toList();

        List<ProductResponse> products = productClient.getProductByIds(productIds);
        if (products == null || products.size() < 1) {
            throw new RuntimeException("Products does not exists");
        }

        Map<Long, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getId, product -> product));

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalPrice = orderDTO
                .getOrderItems()
                .stream()
                .map(orderItemDTO -> {
                    BigDecimal productPrice = productMap.get(orderItemDTO.getProductId()).getPrice();
                    int quantity = orderItemDTO.getQuantity();

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProductId(orderItemDTO.getProductId());
                    orderItem.setQuantity(quantity);

                    orderItems.add(orderItem);

                    return productPrice.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setStatus(OrderStatus.PENDING);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDate.now());
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setUserId(orderDetails.getUserId());
            order.setOrderItems(orderDetails.getOrderItems());
            order.setTotalPrice(orderDetails.getTotalPrice());
            order.setStatus(orderDetails.getStatus());
            order.setOrderDate(orderDetails.getOrderDate());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public Order changeStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
