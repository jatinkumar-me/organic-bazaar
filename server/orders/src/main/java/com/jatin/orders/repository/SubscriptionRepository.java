package com.jatin.orders.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jatin.orders.model.Subscription;
import com.jatin.orders.model.SubscriptionStatus;

/**
 * SubscriptionRepository
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	@Query("SELECT s FROM Subscription s WHERE :dayOfWeek MEMBER OF s.deliveryDays AND s.scheduleType = 'WEEKLY' AND s.status = 'ACTIVE'")
	List<Subscription> findWeeklySubscriptionsForToday(@Param("dayOfWeek") DayOfWeek dayOfWeek);

	@Query("SELECT s FROM Subscription s WHERE s.dayOfMonth = :dayOfMonth AND s.scheduleType = 'MONTHLY' AND s.status = 'ACTIVE'")
	List<Subscription> findMonthlySubscriptionsForToday(@Param("dayOfMonth") int dayOfMonth);

    List<Subscription> findByStatus(SubscriptionStatus active);

    List<Subscription> findByUserId(Long userId);
}
