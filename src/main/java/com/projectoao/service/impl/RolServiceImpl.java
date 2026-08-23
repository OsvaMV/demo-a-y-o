package com.projectoao.service.impl;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.entity.Rol;
import com.projectoao.exception.RecursoNoEncontradoException;
import com.projectoao.mapper.RolMapper;
import com.projectoao.repository.RolRepository;
import com.projectoao.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementacion de {@link RolService}. Envuelve las llamadas JPA (bloqueantes) en
 * {@link Schedulers#boundedElastic()} para no bloquear los hilos reactivos de Netty.
 */
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

	private final RolRepository rolRepository;
	private final RolMapper rolMapper;

	/** {@inheritDoc} */
	@Override
	public Mono<RolDto> guardar(RolRequestDto request) {
		return Mono.fromCallable(() -> {
					Rol rol = rolMapper.toEntity(request);
					return rolMapper.toDto(rolRepository.save(rol));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<RolDto> actualizar(Long id, RolRequestDto request) {
		return Mono.fromCallable(() -> {
					Rol rol = rolRepository.findById(id)
							.orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id " + id));
					rolMapper.actualizarEntidadDesdeDto(request, rol);
					return rolMapper.toDto(rolRepository.save(rol));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<Void> eliminar(Long id) {
		return Mono.<Void>fromRunnable(() -> {
					if (!rolRepository.existsById(id)) {
						throw new RecursoNoEncontradoException("Rol no encontrado con id " + id);
					}
					rolRepository.deleteById(id);
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<RolDto> buscarTodos() {
		return Mono.fromCallable(rolRepository::findAll)
				.subscribeOn(Schedulers.boundedElastic())
				.flatMapMany(Flux::fromIterable)
				.map(rolMapper::toDto);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<RolDto> buscarPorNombre(String nombre) {
		return Mono.fromCallable(() -> rolRepository.findByNombre(nombre)
						.map(rolMapper::toDto)
						.orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con nombre " + nombre)))
				.subscribeOn(Schedulers.boundedElastic());
	}

}
