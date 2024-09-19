package com.jatin.users.model;

import lombok.Data;

@Data
public class AuthenticationResponse {

	private User user;
    private String token;
}
