package com.projectoao.controller;

import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

/**
 * Endpoint de autenticacion: login con username/password para obtener un token JWT.
 */
public interface AuthController {

	/**
	 * Autentica a un usuario y emite un token JWT para consumir el resto de los endpoints.
	 *
	 * @param request username y password del usuario
	 * @return el token emitido junto con datos basicos del usuario autenticado
	 */
	@PostMapping("/auth/login")
	Mono<ResponseEntity<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request);

}
