package com.projectoao.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * Valida el token JWT extraido por {@link JwtAuthenticationConverter} y, si es valido,
 * reconstruye la autenticacion con el username y el rol (como autoridad ROLE_x) del token.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

	private final JwtService jwtService;

	/** {@inheritDoc} */
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token = authentication.getCredentials().toString();
		return Mono.fromCallable(() -> jwtService.validarYObtenerClaims(token))
				.subscribeOn(Schedulers.boundedElastic())
				.map(this::construirAutenticacion)
				.onErrorMap(ex -> !(ex instanceof BadCredentialsException),
						ex -> new BadCredentialsException("Token invalido o expirado", ex));
	}

	private Authentication construirAutenticacion(Claims claims) {
		String username = claims.getSubject();
		String rol = claims.get("rol", String.class);
		if (username == null || rol == null) {
			throw new BadCredentialsException("El token no contiene los datos esperados");
		}
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

}
