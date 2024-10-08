package com.jatin.organic_bazaar_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.jatin.organic_bazaar_gateway.dto.UserDTO;

/**
 * AuthFilter
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private final WebClient.Builder webClientBuilder;

	public AuthFilter(Builder webClientBuilder) {
		super(Config.class);
		this.webClientBuilder = webClientBuilder;
	}

	public static class Config {
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new RuntimeException("Missing authorization information");
			}

			String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

			String[] parts = authHeader.split(" ");

			if (parts.length != 2 || !"Bearer".equals(parts[0])) {
				throw new RuntimeException("Incorrect authorization structure");
			}

			return webClientBuilder.build()
					.get()
					.uri("http://USER-SERVICE/users/verify-token?token=" + parts[1])
					.retrieve().bodyToMono(UserDTO.class)
					.map(userDto -> {
						exchange.getRequest()
								.mutate()
								.header("userId", String.valueOf(userDto.getUserId()));
						return exchange;
					}).flatMap(chain::filter);
		};
	}

}
