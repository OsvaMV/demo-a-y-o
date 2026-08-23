package com.projectoao.controller;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoFiltroDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Endpoints REST para el CRUD de almacenamiento.
 */
public interface AlmacenamientoController {

	/**
	 * Crea un nuevo registro de almacenamiento.
	 *
	 * @param request datos del registro a crear
	 * @return el registro creado, con estado HTTP 201
	 */
	@PostMapping("/almacenamientos")
	Mono<ResponseEntity<AlmacenamientoDto>> guardar(@Valid @RequestBody AlmacenamientoRequestDto request);

	/**
	 * Actualiza un registro de almacenamiento existente.
	 *
	 * @param id identificador del registro a actualizar
	 * @param request nuevos datos del registro
	 * @return el registro actualizado
	 */
	@PutMapping("/almacenamientos/{id}")
	Mono<ResponseEntity<AlmacenamientoDto>> actualizar(@PathVariable Long id,
			@Valid @RequestBody AlmacenamientoRequestDto request);

	/**
	 * Elimina un registro de almacenamiento por su id.
	 *
	 * @param id identificador del registro a eliminar
	 * @return sin contenido (HTTP 204)
	 */
	@DeleteMapping("/almacenamientos/{id}")
	Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id);

	/**
	 * Busca registros de almacenamiento por cualquier combinacion de sus atributos (excepto el
	 * id), recibidos como parametros de consulta opcionales. Sin parametros, devuelve todos.
	 *
	 * @param filtro criterios de busqueda, todos opcionales
	 * @return el flujo de registros que cumplen los filtros informados
	 */
	@GetMapping("/almacenamientos")
	Flux<AlmacenamientoDto> buscar(@ModelAttribute AlmacenamientoFiltroDto filtro);

}
