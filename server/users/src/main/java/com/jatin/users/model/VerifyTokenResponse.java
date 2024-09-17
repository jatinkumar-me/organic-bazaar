package com.jatin.users.model;

import lombok.Data;

/**
 * VerifyTokenResponse
 */
@Data
public class VerifyTokenResponse {

	private Long userId;
	private String token;
}
