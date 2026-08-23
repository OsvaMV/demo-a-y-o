package com.projectoao.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Extrae el token JWT del header {@code Authorization: Bearer <token>} de cada request. Si no
 * hay header (o no trae el prefijo Bearer), no hay nada que autenticar en este request.
 */
@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

	private static final String PREFIJO_BEARER = "Bearer ";

	/** {@inheritDoc} */
	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(PREFIJO_BEARER)) {
			return Mono.empty();
		}
		String token = header.substring(PREFIJO_BEARER.length());
		return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
	}

}
