package com.projectoao.service;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoFiltroDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Operaciones de negocio disponibles para el recurso almacenamiento.
 */
public interface AlmacenamientoService {

	/**
	 * Crea un nuevo registro de almacenamiento.
	 *
	 * @param request datos del registro a crear
	 * @return el registro creado
	 */
	Mono<AlmacenamientoDto> guardar(AlmacenamientoRequestDto request);

	/**
	 * Actualiza un registro de almacenamiento existente.
	 *
	 * @param id identificador del registro a actualizar
	 * @param request nuevos datos del registro
	 * @return el registro actualizado
	 */
	Mono<AlmacenamientoDto> actualizar(Long id, AlmacenamientoRequestDto request);

	/**
	 * Elimina un registro de almacenamiento por su id.
	 *
	 * @param id identificador del registro a eliminar
	 */
	Mono<Void> eliminar(Long id);

	/**
	 * Busca registros de almacenamiento por cualquier combinacion de sus atributos (excepto el
	 * id). Los filtros no informados se ignoran; si no se envia ninguno, devuelve todos.
	 *
	 * @param filtro criterios de busqueda, todos opcionales
	 * @return el flujo de registros que cumplen los filtros informados
	 */
	Flux<AlmacenamientoDto> buscar(AlmacenamientoFiltroDto filtro);

}
