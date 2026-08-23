package com.projectoao.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Implementacion de {@link JwtService} basada en la libreria jjwt (HMAC-SHA).
 *
 * <p>El constructor se escribe a mano (no con {@code @RequiredArgsConstructor}) porque la clave
 * y la expiracion se inyectan con {@code @Value} por parametro, algo que Lombok no puede generar
 * en un constructor de campos requeridos.
 */
@Service
public class JwtServiceImpl implements JwtService {

	private static final String CLAIM_ROL = "rol";

	private final SecretKey clave;
	private final long expiracionMinutos;

	public JwtServiceImpl(
			@Value("${security.jwt.secret}") String secreto,
			@Value("${security.jwt.expiration-minutes:60}") long expiracionMinutos) {
		this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
		this.expiracionMinutos = expiracionMinutos;
	}

	/** {@inheritDoc} */
	@Override
	public String generarToken(String username, String rol) {
		Instant ahora = Instant.now();
		return Jwts.builder()
				.subject(username)
				.claim(CLAIM_ROL, rol)
				.issuedAt(Date.from(ahora))
				.expiration(Date.from(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES)))
				.signWith(clave)
				.compact();
	}

	/** {@inheritDoc} */
	@Override
	public Claims validarYObtenerClaims(String token) {
		return Jwts.parser()
				.verifyWith(clave)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/** {@inheritDoc} */
	@Override
	public long getExpiracionMinutos() {
		return expiracionMinutos;
	}

}
