package com.projectoao.controller.impl;

import com.projectoao.controller.AuthController;
import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import com.projectoao.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Implementacion de {@link AuthController}.
 */
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

	private final AuthService authService;

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<LoginResponseDto>> login(LoginRequestDto request) {
		return authService.login(request).map(ResponseEntity::ok);
	}

}
