package com.jatin.orders.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import com.jatin.orders.model.DeliveryScheduleType;

import lombok.Data;

/**
 * SubscriptionDTO
 */
@Data
public class SubscriptionDTO {
	
    private Long productId;
	private int quantity;
    private DeliveryScheduleType scheduleType;
    private Set<DayOfWeek> deliveryDays;
    private Integer dayOfMonth;
	private LocalDate startDate = LocalDate.now();
	private LocalDate endDate = LocalDate.of(9999, 12, 31);
}
