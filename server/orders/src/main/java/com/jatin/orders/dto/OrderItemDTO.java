package com.jatin.orders.dto;

import lombok.Data;

/**
 * OrderItemDTO
 */
@Data
public class OrderItemDTO {

	private Long productId;
	private int quantity;
}
