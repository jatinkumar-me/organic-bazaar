package com.jatin.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.products.model.Review;

/**
 * ReviewRepository
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);
}
