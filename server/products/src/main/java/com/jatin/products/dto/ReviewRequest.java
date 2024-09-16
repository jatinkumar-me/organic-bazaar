package com.jatin.products.dto;

import lombok.Data;

/**
 * ReviewRequest.java
 */
@Data
public class ReviewRequest {

    private String comment;
    private int rating;
}
