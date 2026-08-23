package com.projectoao.service;

import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Operaciones de negocio disponibles para el recurso usuario.
 */
public interface UsuarioService {

	/**
	 * Crea un nuevo usuario.
	 *
	 * @param request datos del usuario a crear
	 * @return el usuario creado
	 */
	Mono<UsuarioDto> guardar(UsuarioRequestDto request);

	/**
	 * Actualiza un usuario existente.
	 *
	 * @param id identificador del usuario a actualizar
	 * @param request nuevos datos del usuario
	 * @return el usuario actualizado
	 */
	Mono<UsuarioDto> actualizar(Long id, UsuarioRequestDto request);

	/**
	 * Elimina un usuario por su id.
	 *
	 * @param id identificador del usuario a eliminar
	 */
	Mono<Void> eliminar(Long id);

	/**
	 * Lista todos los usuarios.
	 *
	 * @return el flujo de usuarios registrados
	 */
	Flux<UsuarioDto> buscarTodos();

	/**
	 * Busca usuarios cuyo username contenga el texto dado (coincidencia parcial, sin
	 * distinguir mayusculas).
	 *
	 * @param username texto a buscar dentro del username
	 * @return el flujo de usuarios que coinciden
	 */
	Flux<UsuarioDto> buscarPorUsername(String username);

}
