package com.jatin.orders.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jatin.orders.dto.OrderDTO;
import com.jatin.orders.model.Order;
import com.jatin.orders.model.OrderStatus;
import com.jatin.orders.service.OrderService;

/**
 * OrderController
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestHeader String userId, @RequestBody OrderDTO orderDTO) {
		Order createdOrder = orderService.createOrder(Long.parseLong(userId), orderDTO);
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
	}

	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @RequestHeader String userId) {
		Order mutatedOrder = orderService.changeStatus(orderId, OrderStatus.CANCELLED);
		return new ResponseEntity<>(mutatedOrder, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Order>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Order>> getUserOrders(@RequestHeader String userId) {
		List<Order> orders = orderService.getOrdersByUserId(Long.parseLong(userId));
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
		Order order = orderService.getOrderById(orderId);
		if (order != null) {
			return new ResponseEntity<>(order, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
		orderService.deleteOrder(orderId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
