package com.projectoao.controller.impl;

import com.projectoao.controller.UsuarioController;
import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
import com.projectoao.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de {@link UsuarioController}.
 */
@RestController
@RequiredArgsConstructor
public class UsuarioControllerImpl implements UsuarioController {

	private final UsuarioService usuarioService;

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<UsuarioDto>> guardar(UsuarioRequestDto request) {
		return usuarioService.guardar(request)
				.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<UsuarioDto>> actualizar(Long id, UsuarioRequestDto request) {
		return usuarioService.actualizar(id, request)
				.map(ResponseEntity::ok);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Void>> eliminar(Long id) {
		return usuarioService.eliminar(id)
				.thenReturn(ResponseEntity.noContent().build());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<UsuarioDto> buscarTodos() {
		return usuarioService.buscarTodos();
	}

	/** {@inheritDoc} */
	@Override
	public Flux<UsuarioDto> buscarPorUsername(String username) {
		return usuarioService.buscarPorUsername(username);
	}

}
