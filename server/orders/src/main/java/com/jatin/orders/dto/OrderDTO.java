package com.jatin.orders.dto;

import java.util.List;

import lombok.Data;

/**
 * OrderDTO
 */
@Data
public class OrderDTO {

	private List<OrderItemDTO> orderItems;
}
