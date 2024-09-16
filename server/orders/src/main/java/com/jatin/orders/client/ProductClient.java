package com.jatin.orders.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.jatin.orders.dto.ProductResponse;

/**
 * ProductClient
 */
@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("/ids")
	List<ProductResponse> getProductByIds(@RequestParam List<Long> ids);

	@GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable("id") Long productId);
}
