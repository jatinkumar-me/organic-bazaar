package com.jatin.organic_bazaar_gateway.dto;

/**
 * UserDTO
 */
public class UserDTO {

	private Long userId;
	private String token;

	public UserDTO() {
	}

	public UserDTO(Long id, String token) {
		this.userId = id;
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long id) {
		this.userId = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + userId + ", token=" + token + "]";
	}
	
}
