package com.jatin.orders.dto;

import java.time.LocalDate;

import com.jatin.orders.model.SubscriptionStatus;

import lombok.Data;

/**
 * SubscriptionStatusDTO
 */
@Data
public class SubscriptionStatusDTO {

	private SubscriptionStatus status;
	private LocalDate pauseTill;
}
