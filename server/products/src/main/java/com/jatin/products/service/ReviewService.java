package com.jatin.products.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jatin.products.dto.ReviewRequest;
import com.jatin.products.model.Product;
import com.jatin.products.model.Review;
import com.jatin.products.repository.ProductRepository;
import com.jatin.products.repository.ReviewRepository;

/**
 * ReviewService
 */
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public Review addReview(Long userId, Long productId, ReviewRequest reviewRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setUserId(userId);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        List<Review> productReviews = reviewRepository.findByProductId(productId);
        return productReviews;
    }

    public void deleteReview(Long userId, Long id) {
        Long reviewUserId = reviewRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Review doesn't exist"))
                .getUserId();
        if (userId != reviewUserId) {
            throw new RuntimeException("Unauthorized");
        }
        reviewRepository.deleteById(id);
    }

    public Review updateReview(Long userId, Long id, ReviewRequest reviewRequest) {
        Review oldReview = reviewRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Review doesn't exist"));
        if (userId != oldReview.getUserId()) {
            throw new RuntimeException("Unauthorized");
        }
        oldReview.setRating(reviewRequest.getRating());
        oldReview.setComment(reviewRequest.getComment());
        return reviewRepository.save(oldReview);
    }
}
