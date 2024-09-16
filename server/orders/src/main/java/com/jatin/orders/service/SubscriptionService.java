package com.jatin.orders.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jatin.orders.client.ProductClient;
import com.jatin.orders.dto.ProductResponse;
import com.jatin.orders.dto.SubscriptionDTO;
import com.jatin.orders.model.Subscription;
import com.jatin.orders.model.SubscriptionStatus;
import com.jatin.orders.repository.SubscriptionRepository;

/**
 * SubscriptionService
 */
@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    ProductClient productClient;

    @Autowired
    OrderService orderService;

    public Subscription createSubscription(Long userId, SubscriptionDTO subscriptionDto) {
        Subscription subscription = new Subscription();
        Long productId = subscriptionDto.getProductId();
        int quantity = subscriptionDto.getQuantity();

        ProductResponse products = productClient.getProductById(productId);
        if (products == null) {
            throw new RuntimeException("Products does not exists");
        }
        // BigDecimal totalPrice =
        // products.getPrice().multiply(BigDecimal.valueOf(quantity));

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setUserId(userId);
        subscription.setProductId(productId);
        subscription.setQuantity(quantity);
        subscription.setStartDate(subscriptionDto.getStartDate());
        subscription.setEndDate(subscriptionDto.getEndDate());
        subscription.setScheduleType(subscriptionDto.getScheduleType());
        subscription.setDayOfMonth(subscriptionDto.getDayOfMonth());
        subscription.setDeliveryDays(subscriptionDto.getDeliveryDays());
        updateNextDeliveryDate(subscription);

        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscriptionStatus(Long subscriptionId, SubscriptionStatus subscriptionStatus) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found with it " + subscriptionId));
        subscription.setStatus(subscriptionStatus);
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCron() {
        this.updateSubscriptionStatusForExpired();
        this.checkSubscriptionsForDelivery();
    }

    private void updateSubscriptionStatusForExpired() {
        LocalDate today = LocalDate.now();
        List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
        List<Subscription> subscriptionsToUpdate = new ArrayList<>();

        for (Subscription subscription : activeSubscriptions) {
            if (subscription.getEndDate() != null && subscription.getEndDate().isBefore(today)) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionsToUpdate.add(subscription);
            }
        }
        subscriptionRepository.saveAll(subscriptionsToUpdate);
    }

    public void checkSubscriptionsForDelivery() {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        int todayDayOfMonth = today.getDayOfMonth();

        List<Subscription> weeklySubscriptions = subscriptionRepository
                .findWeeklySubscriptionsForToday(todayDayOfWeek);

        List<Subscription> monthlySubscriptions = subscriptionRepository
                .findMonthlySubscriptionsForToday(todayDayOfMonth);

        for (Subscription subscription : weeklySubscriptions) {
            processDelivery(subscription);
        }
        for (Subscription subscription : monthlySubscriptions) {
            processDelivery(subscription);
        }
    }

    private void processDelivery(Subscription subscription) {
        updateNextDeliveryDate(subscription);
    }

    private void updateNextDeliveryDate(Subscription subscription) {
        switch (subscription.getScheduleType()) {
            case WEEKLY:
                subscription.setNextDeliveryDate(calculateNextWeeklyDelivery(subscription.getDeliveryDays()));
                break;
            case MONTHLY:
                subscription.setNextDeliveryDate(calculateNextMonthlyDelivery(subscription.getDayOfMonth()));
                break;
            default:
                break;
        }
    }

    private LocalDate calculateNextWeeklyDelivery(Set<DayOfWeek> deliveryDays) {
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate potentialDeliveryDate = currentDate.plusDays(i);
            if (deliveryDays.contains(potentialDeliveryDate.getDayOfWeek())) {
                return potentialDeliveryDate;
            }
        }
        return null;
    }

    private LocalDate calculateNextMonthlyDelivery(int dayOfMonth) {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstOfNextMonth = currentDate.withDayOfMonth(1).plusMonths(1);
        if (currentDate.getDayOfMonth() <= dayOfMonth) {
            return currentDate.withDayOfMonth(dayOfMonth);
        }
        return firstOfNextMonth.withDayOfMonth(Math.min(dayOfMonth, firstOfNextMonth.lengthOfMonth()));
    }

    public void deleteSubscription(Long subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }
}
