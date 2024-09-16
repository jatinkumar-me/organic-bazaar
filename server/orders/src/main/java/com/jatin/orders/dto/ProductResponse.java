package com.jatin.orders.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * ProductResponse
 */
@Data
public class ProductResponse {

	private Long id;
	private String name;
	private String category;
	private BigDecimal price;
	private Integer stock;
}
