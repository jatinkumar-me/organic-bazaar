package com.jatin.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jatin.products.dto.ReviewRequest;
import com.jatin.products.model.Review;
import com.jatin.products.service.ReviewService;

/**
 * ReviewController
 */
@RestController
@RequestMapping("products/{productId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getReviewsByProductId(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PostMapping
    public Review addReview(@RequestHeader("userId") String userId, @PathVariable Long productId,
            @RequestBody ReviewRequest review) {
        return reviewService.addReview(Long.parseLong(userId), productId, review);
    }

    @PatchMapping("/{id}")
    public Review updateReview(@RequestHeader("userId") String userId, @PathVariable Long id,
            @RequestBody ReviewRequest reviewRequest) {
        return reviewService.updateReview(Long.parseLong(userId), id, reviewRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@RequestHeader("userId") String userId, @PathVariable Long id) {
        reviewService.deleteReview(Long.parseLong(userId), id);
    }
}
