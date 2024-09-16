package com.jatin.orders.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Subscription
 */
@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

	@Column(nullable = false)
	private int quantity = 1;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryScheduleType scheduleType;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "subscription_delivery_days")
    @Column(name = "delivery_day")
    private Set<DayOfWeek> deliveryDays;

    @Column(nullable = true)
    private Integer dayOfMonth;

    // @Column(nullable = true)
    // private BigDecimal subscriptionPrice;

    @Column(nullable = true)
    private LocalDate nextDeliveryDate;
}
