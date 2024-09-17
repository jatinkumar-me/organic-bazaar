package com.jatin.organic_bazaar_gateway.dto;

/**
 * UserDTO
 */
public class UserDTO {

	private Long id;
	private String token;

	public UserDTO() {
	}

	public UserDTO(Long id, String token) {
		this.id = id;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
