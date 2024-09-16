package com.jatin.users.model;

import lombok.Data;

/**
 * AuthenticationRequest
 */
@Data
public class AuthenticationRequest {

	private String email;
	private String password;
}
