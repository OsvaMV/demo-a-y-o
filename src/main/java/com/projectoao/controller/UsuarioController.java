package com.projectoao.controller;

import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
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
 * Endpoints REST para el CRUD de usuarios.
 */
public interface UsuarioController {

	/**
	 * Crea un nuevo usuario.
	 *
	 * @param request datos del usuario a crear
	 * @return el usuario creado, con estado HTTP 201
	 */
	@PostMapping("/usuarios")
	Mono<ResponseEntity<UsuarioDto>> guardar(@Valid @RequestBody UsuarioRequestDto request);

	/**
	 * Actualiza un usuario existente.
	 *
	 * @param id identificador del usuario a actualizar
	 * @param request nuevos datos del usuario
	 * @return el usuario actualizado
	 */
	@PutMapping("/usuarios/{id}")
	Mono<ResponseEntity<UsuarioDto>> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto request);

	/**
	 * Elimina un usuario por su id.
	 *
	 * @param id identificador del usuario a eliminar
	 * @return sin contenido (HTTP 204)
	 */
	@DeleteMapping("/usuarios/{id}")
	Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id);

	/**
	 * Lista todos los usuarios.
	 *
	 * @return el flujo de usuarios registrados
	 */
	@GetMapping("/usuarios")
	Flux<UsuarioDto> buscarTodos();

	/**
	 * Busca un usuario por su username.
	 *
	 * @param username username exacto del usuario
	 * @return el usuario encontrado
	 */
	@GetMapping("/usuarios/username/{username}")
	Mono<ResponseEntity<UsuarioDto>> buscarPorUsername(@PathVariable String username);

}
