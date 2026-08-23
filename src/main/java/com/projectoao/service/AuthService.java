package com.projectoao.service;

import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import reactor.core.publisher.Mono;

/**
 * Operaciones de autenticacion disponibles para obtener un token JWT.
 */
public interface AuthService {

	/**
	 * Valida las credenciales de un usuario y, si son correctas, emite un token JWT.
	 *
	 * @param request username y password a validar
	 * @return el token emitido junto con datos basicos del usuario autenticado
	 */
	Mono<LoginResponseDto> login(LoginRequestDto request);

}
