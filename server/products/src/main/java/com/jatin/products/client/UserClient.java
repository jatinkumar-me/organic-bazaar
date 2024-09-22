package com.jatin.products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * UserClient
 */
@FeignClient(name = "user-service")
public interface UserClient {

	@GetMapping("/users/{userId}")
	Boolean validateUser(@PathVariable("userId") Long userId);
}
