package com.projectoao.service.impl;

import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import com.projectoao.exception.CredencialesInvalidasException;
import com.projectoao.security.JwtService;
import com.projectoao.security.UsuarioDetailsService;
import com.projectoao.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementacion de {@link AuthService}. Reutiliza {@link UsuarioDetailsService} para cargar el
 * usuario y {@link PasswordEncoder} para validar la contrasena antes de emitir el token.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UsuarioDetailsService usuarioDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	/** {@inheritDoc} */
	@Override
	public Mono<LoginResponseDto> login(LoginRequestDto request) {
		return usuarioDetailsService.findByUsername(request.getUsername())
				.filter(userDetails -> userDetails.isEnabled()
						&& passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
				.switchIfEmpty(Mono.error(new CredencialesInvalidasException("Usuario o contrasena invalidos")))
				.map(this::generarRespuesta);
	}

	private LoginResponseDto generarRespuesta(UserDetails userDetails) {
		String rol = extraerRol(userDetails);
		String token = jwtService.generarToken(userDetails.getUsername(), rol);

		LoginResponseDto response = new LoginResponseDto();
		response.setToken(token);
		response.setTokenType("Bearer");
		response.setUsername(userDetails.getUsername());
		response.setRol(rol);
		response.setExpiraEnMinutos(jwtService.getExpiracionMinutos());
		return response;
	}

	private String extraerRol(UserDetails userDetails) {
		return userDetails.getAuthorities().stream()
				.findFirst()
				.map(GrantedAuthority::getAuthority)
				.map(authority -> authority.replaceFirst("^ROLE_", ""))
				.orElseThrow(() -> new CredencialesInvalidasException("El usuario no tiene un rol asignado"));
	}

}
