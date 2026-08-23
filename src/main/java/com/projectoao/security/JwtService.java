package com.projectoao.security;

import io.jsonwebtoken.Claims;

/**
 * Generacion y validacion de tokens JWT usados para autenticar requests una vez hecho el login.
 */
public interface JwtService {

	/**
	 * Genera un token JWT firmado para un usuario autenticado.
	 *
	 * @param username username del usuario
	 * @param rol nombre del rol del usuario (sin el prefijo ROLE_)
	 * @return el token compacto firmado
	 */
	String generarToken(String username, String rol);

	/**
	 * Valida la firma y expiracion de un token y devuelve sus claims.
	 *
	 * @param token token JWT recibido en el header Authorization
	 * @return los claims contenidos en el token
	 * @throws io.jsonwebtoken.JwtException si el token es invalido, esta corrupto o expiro
	 */
	Claims validarYObtenerClaims(String token);

	/**
	 * Minutos de vigencia configurados para los tokens emitidos.
	 *
	 * @return los minutos de expiracion
	 */
	long getExpiracionMinutos();

}
