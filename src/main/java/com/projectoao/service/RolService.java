package com.projectoao.service;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Operaciones de negocio disponibles para el recurso rol.
 */
public interface RolService {

	/**
	 * Crea un nuevo rol.
	 *
	 * @param request datos del rol a crear
	 * @return el rol creado
	 */
	Mono<RolDto> guardar(RolRequestDto request);

	/**
	 * Actualiza un rol existente.
	 *
	 * @param id identificador del rol a actualizar
	 * @param request nuevos datos del rol
	 * @return el rol actualizado
	 */
	Mono<RolDto> actualizar(Long id, RolRequestDto request);

	/**
	 * Elimina un rol por su id.
	 *
	 * @param id identificador del rol a eliminar
	 */
	Mono<Void> eliminar(Long id);

	/**
	 * Lista todos los roles.
	 *
	 * @return el flujo de roles registrados
	 */
	Flux<RolDto> buscarTodos();

	/**
	 * Busca un rol por su nombre.
	 *
	 * @param nombre nombre exacto del rol
	 * @return el rol encontrado
	 */
	Mono<RolDto> buscarPorNombre(String nombre);

}
