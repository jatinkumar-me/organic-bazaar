package com.jatin.products.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT p FROM Product p WHERE " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> findByCategoryAndSearch(
            @Param("category") String category,
            @Param("search") String search,
            Sort sort);
    
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findUniqueCategories();
}
