package com.jatin.products.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.products.model.Product;

/**
 * ProductRepository
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByIdIn(List<Long> ids);

	List<Product> findByCategory(String category);

	List<Product> findByStockGreaterThan(Integer stock);

	List<Product> findByPriceLessThan(BigDecimal price);

	List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
