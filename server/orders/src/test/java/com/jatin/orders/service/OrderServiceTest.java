package com.jatin.orders.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jatin.orders.client.ProductClient;
import com.jatin.orders.dto.OrderDTO;
import com.jatin.orders.dto.OrderItemDTO;
import com.jatin.orders.dto.ProductResponse;
import com.jatin.orders.model.Order;
import com.jatin.orders.model.OrderStatus;
import com.jatin.orders.repository.OrderRepository;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(orderId);

        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById(orderId);
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testCreateOrder() {
        Long userId = 1L;

        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setProductId(1L);
        orderItemDTO1.setQuantity(2);

        OrderItemDTO orderItemDTO2 = new OrderItemDTO();
        orderItemDTO2.setProductId(2L);
        orderItemDTO2.setQuantity(1);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderItems(Arrays.asList(orderItemDTO1, orderItemDTO2));

        ProductResponse product1 = new ProductResponse();
        ProductResponse product2 = new ProductResponse();

        when(productClient.getProductByIds(any())).thenReturn(Arrays.asList(product1, product2));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order createdOrder = orderService.createOrder(userId, orderDTO);

        assertNotNull(createdOrder);
        assertEquals(BigDecimal.valueOf(400), createdOrder.getTotalPrice());
        assertEquals(2, createdOrder.getOrderItems().size());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productClient, times(1)).getProductByIds(any());
    }

    @Test
    void testCreateOrder_ProductsNotFound() {
        when(productClient.getProductByIds(any())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, new OrderDTO());
        });

        assertEquals("Products does not exists", exception.getMessage());
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        Order oldOrder = new Order();
        oldOrder.setId(orderId);
        oldOrder.setTotalPrice(BigDecimal.valueOf(500));

        Order updatedOrderDetails = new Order();
        updatedOrderDetails.setTotalPrice(BigDecimal.valueOf(600));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(oldOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order updatedOrder = orderService.updateOrder(orderId, updatedOrderDetails);

        assertNotNull(updatedOrder);
        assertEquals(BigDecimal.valueOf(600), updatedOrder.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_NotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateOrder(orderId, new Order());
        });

        assertEquals("Order not found with id " + orderId, exception.getMessage());
    }

    @Test
    void testChangeStatus() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order updatedOrder = orderService.changeStatus(orderId, OrderStatus.DELIVERED);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testChangeStatus_NotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.changeStatus(orderId, OrderStatus.DELIVERED);
        });

        assertEquals("Order not found with id " + orderId, exception.getMessage());
    }
}
