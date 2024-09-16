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

import com.jatin.orders.dto.SubscriptionDTO;
import com.jatin.orders.dto.SubscriptionStatusDTO;
import com.jatin.orders.model.Subscription;
import com.jatin.orders.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@PostMapping
	public ResponseEntity<Subscription> createSubscription(@RequestHeader("userId") String userId,
			@RequestBody SubscriptionDTO subscriptionDTO) {
		Subscription createdSubscription = subscriptionService.createSubscription(Long.parseLong(userId),
				subscriptionDTO);
		return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Subscription>> getAllSubscriptions(@RequestHeader("userId") String userId) {
		if (userId != null) {
			List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(Long.parseLong(userId));
			return new ResponseEntity<>(subscriptions, HttpStatus.OK);
		}
		List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
		return new ResponseEntity<>(subscriptions, HttpStatus.OK);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Subscription> updateSubscriptionStatus(@PathVariable Long id,
			@RequestBody SubscriptionStatusDTO subscriptionStatusDTO) {
		Subscription updatedSubscription = subscriptionService.updateSubscriptionStatus(id, subscriptionStatusDTO.getStatus());
		if (updatedSubscription != null) {
			return new ResponseEntity<>(updatedSubscription, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
		subscriptionService.deleteSubscription(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
