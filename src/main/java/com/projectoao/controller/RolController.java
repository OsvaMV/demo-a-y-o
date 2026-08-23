package com.projectoao.controller;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Endpoints REST para el CRUD de roles.
 */
public interface RolController {

	/**
	 * Crea un nuevo rol.
	 *
	 * @param request datos del rol a crear
	 * @return el rol creado, con estado HTTP 201
	 */
	@PostMapping("/roles")
	Mono<ResponseEntity<RolDto>> guardar(@Valid @RequestBody RolRequestDto request);

	/**
	 * Actualiza un rol existente.
	 *
	 * @param id identificador del rol a actualizar
	 * @param request nuevos datos del rol
	 * @return el rol actualizado
	 */
	@PutMapping("/roles/{id}")
	Mono<ResponseEntity<RolDto>> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequestDto request);

	/**
	 * Elimina un rol por su id.
	 *
	 * @param id identificador del rol a eliminar
	 * @return sin contenido (HTTP 204)
	 */
	@DeleteMapping("/roles/{id}")
	Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id);

	/**
	 * Lista todos los roles.
	 *
	 * @return el flujo de roles registrados
	 */
	@GetMapping("/roles")
	Flux<RolDto> buscarTodos();

	/**
	 * Busca roles cuyo nombre contenga el texto dado (coincidencia parcial, sin
	 * distinguir mayusculas).
	 *
	 * @param nombre texto a buscar dentro del nombre del rol
	 * @return el flujo de roles que coinciden
	 */
	@GetMapping("/roles/nombre/{nombre}")
	Flux<RolDto> buscarPorNombre(@PathVariable String nombre);

}
