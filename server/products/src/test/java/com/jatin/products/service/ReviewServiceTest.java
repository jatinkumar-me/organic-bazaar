package com.jatin.products.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jatin.products.dto.ReviewRequest;
import com.jatin.products.model.Product;
import com.jatin.products.model.Review;
import com.jatin.products.repository.ProductRepository;
import com.jatin.products.repository.ReviewRepository;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddReview() {
        Long userId = 1L;
        Long productId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Great product");
        reviewRequest.setRating(5);

        Product product = new Product();
        product.setId(productId);

        Review review = new Review();
        review.setId(1L);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setProduct(product);
        review.setUserId(userId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review savedReview = reviewService.addReview(userId, productId, reviewRequest);

        assertNotNull(savedReview);
        assertEquals("Great product", savedReview.getComment());
        assertEquals(5, savedReview.getRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testAddReviewProductNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.addReview(1L, productId, new ReviewRequest());
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testGetReviewsByProductId() {
        Long productId = 1L;

        Review review1 = new Review();
        review1.setId(1L);
        review1.setComment("Review 1");

        Review review2 = new Review();
        review2.setId(2L);
        review2.setComment("Review 2");

        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findByProductId(productId)).thenReturn(reviews);

        List<Review> productReviews = reviewService.getReviewsByProductId(productId);

        assertNotNull(productReviews);
        assertEquals(2, productReviews.size());
        assertEquals("Review 1", productReviews.get(0).getComment());
    }

    @Test
    void testDeleteReview() {
        Long userId = 1L;
        Long reviewId = 1L;

        Review review = new Review();
        review.setId(reviewId);
        review.setUserId(userId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.deleteReview(userId, reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testDeleteReviewUnauthorized() {
        Long userId = 1L;
        Long reviewId = 1L;

        Review review = new Review();
        review.setId(reviewId);
        review.setUserId(2L); // Different user

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.deleteReview(userId, reviewId);
        });

        assertEquals("Unauthorized", exception.getMessage());
        verify(reviewRepository, never()).deleteById(reviewId);
    }

    @Test
    void testUpdateReview() {
        Long userId = 1L;
        Long reviewId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Updated comment");
        reviewRequest.setRating(4);

        Review existingReview = new Review();
        existingReview.setId(reviewId);
        existingReview.setUserId(userId);
        existingReview.setComment("Old comment");
        existingReview.setRating(5);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        Review updatedReview = reviewService.updateReview(userId, reviewId, reviewRequest);

        assertNotNull(updatedReview);
        assertEquals("Updated comment", updatedReview.getComment());
        assertEquals(4, updatedReview.getRating());
        verify(reviewRepository, times(1)).save(existingReview);
    }

    @Test
    void testUpdateReviewUnauthorized() {
        Long userId = 1L;
        Long reviewId = 1L;

        Review existingReview = new Review();
        existingReview.setId(reviewId);
        existingReview.setUserId(2L); // Different user

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview(userId, reviewId, new ReviewRequest());
        });

        assertEquals("Unauthorized", exception.getMessage());
        verify(reviewRepository, never()).save(existingReview);
    }
}
