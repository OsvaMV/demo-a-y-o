package com.projectoao.controller.impl;

import com.projectoao.controller.AlmacenamientoController;
import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoFiltroDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import com.projectoao.service.AlmacenamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de {@link AlmacenamientoController}.
 */
@RestController
@RequiredArgsConstructor
public class AlmacenamientoControllerImpl implements AlmacenamientoController {

	private final AlmacenamientoService almacenamientoService;

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<AlmacenamientoDto>> guardar(AlmacenamientoRequestDto request) {
		return almacenamientoService.guardar(request)
				.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<AlmacenamientoDto>> actualizar(Long id, AlmacenamientoRequestDto request) {
		return almacenamientoService.actualizar(id, request)
				.map(ResponseEntity::ok);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Void>> eliminar(Long id) {
		return almacenamientoService.eliminar(id)
				.thenReturn(ResponseEntity.noContent().build());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<AlmacenamientoDto> buscar(AlmacenamientoFiltroDto filtro) {
		return almacenamientoService.buscar(filtro);
	}

}
