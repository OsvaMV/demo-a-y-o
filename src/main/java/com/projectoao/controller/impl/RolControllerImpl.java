package com.projectoao.controller.impl;

import com.projectoao.controller.RolController;
import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de {@link RolController}.
 */
@RestController
@RequiredArgsConstructor
public class RolControllerImpl implements RolController {

	private final RolService rolService;

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<RolDto>> guardar(RolRequestDto request) {
		return rolService.guardar(request)
				.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<RolDto>> actualizar(Long id, RolRequestDto request) {
		return rolService.actualizar(id, request)
				.map(ResponseEntity::ok);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Void>> eliminar(Long id) {
		return rolService.eliminar(id)
				.thenReturn(ResponseEntity.noContent().build());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<RolDto> buscarTodos() {
		return rolService.buscarTodos();
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<RolDto>> buscarPorNombre(String nombre) {
		return rolService.buscarPorNombre(nombre)
				.map(ResponseEntity::ok);
	}

}
