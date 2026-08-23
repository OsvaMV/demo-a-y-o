package com.projectoao.dto;

import lombok.Data;

/**
 * DTO de respuesta de un login exitoso, con el token JWT a usar en el header
 * {@code Authorization: Bearer <token>} del resto de endpoints.
 */
@Data
public class LoginResponseDto {

	/** Token JWT firmado. */
	private String token;

	/** Tipo de token, siempre {@code Bearer}. */
	private String tokenType;

	/** Username del usuario autenticado. */
	private String username;

	/** Rol del usuario autenticado (sin el prefijo ROLE_). */
	private String rol;

	/** Minutos que faltan para que el token expire desde que se emitio. */
	private long expiraEnMinutos;

}
