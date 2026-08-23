package com.projectoao.service.impl;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoFiltroDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import com.projectoao.entity.Almacenamiento;
import com.projectoao.exception.RecursoNoEncontradoException;
import com.projectoao.mapper.AlmacenamientoMapper;
import com.projectoao.repository.AlmacenamientoRepository;
import com.projectoao.service.AlmacenamientoService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion de {@link AlmacenamientoService}. Envuelve las llamadas JPA (bloqueantes) en
 * {@link Schedulers#boundedElastic()} para no bloquear los hilos reactivos de Netty.
 */
@Service
@RequiredArgsConstructor
public class AlmacenamientoServiceImpl implements AlmacenamientoService {

	private final AlmacenamientoRepository almacenamientoRepository;
	private final AlmacenamientoMapper almacenamientoMapper;

	/** {@inheritDoc} */
	@Override
	public Mono<AlmacenamientoDto> guardar(AlmacenamientoRequestDto request) {
		return Mono.fromCallable(() -> {
					Almacenamiento almacenamiento = almacenamientoMapper.toEntity(request);
					return almacenamientoMapper.toDto(almacenamientoRepository.save(almacenamiento));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<AlmacenamientoDto> actualizar(Long id, AlmacenamientoRequestDto request) {
		return Mono.fromCallable(() -> {
					Almacenamiento almacenamiento = almacenamientoRepository.findById(id)
							.orElseThrow(() -> new RecursoNoEncontradoException(
									"Almacenamiento no encontrado con id " + id));
					almacenamientoMapper.actualizarEntidadDesdeDto(request, almacenamiento);
					return almacenamientoMapper.toDto(almacenamientoRepository.save(almacenamiento));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<Void> eliminar(Long id) {
		return Mono.<Void>fromRunnable(() -> {
					if (!almacenamientoRepository.existsById(id)) {
						throw new RecursoNoEncontradoException("Almacenamiento no encontrado con id " + id);
					}
					almacenamientoRepository.deleteById(id);
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<AlmacenamientoDto> buscar(AlmacenamientoFiltroDto filtro) {
		return Mono.fromCallable(() -> almacenamientoRepository.findAll(construirSpecification(filtro)))
				.subscribeOn(Schedulers.boundedElastic())
				.flatMapMany(Flux::fromIterable)
				.map(almacenamientoMapper::toDto);
	}

	private Specification<Almacenamiento> construirSpecification(AlmacenamientoFiltroDto filtro) {
		return (root, query, cb) -> {
			List<Predicate> predicados = new ArrayList<>();

			if (StringUtils.hasText(filtro.getObjetoAlmacenado())) {
				predicados.add(cb.like(cb.lower(root.get("objetoAlmacenado")),
						"%" + filtro.getObjetoAlmacenado().toLowerCase() + "%"));
			}
			if (filtro.getFechaIngresoDesde() != null) {
				predicados.add(cb.greaterThanOrEqualTo(root.get("fechaIngreso"), filtro.getFechaIngresoDesde()));
			}
			if (filtro.getFechaIngresoHasta() != null) {
				predicados.add(cb.lessThanOrEqualTo(root.get("fechaIngreso"), filtro.getFechaIngresoHasta()));
			}
			if (filtro.getFechaSalidaDesde() != null) {
				predicados.add(cb.greaterThanOrEqualTo(root.get("fechaSalida"), filtro.getFechaSalidaDesde()));
			}
			if (filtro.getFechaSalidaHasta() != null) {
				predicados.add(cb.lessThanOrEqualTo(root.get("fechaSalida"), filtro.getFechaSalidaHasta()));
			}

			return cb.and(predicados.toArray(new Predicate[0]));
		};
	}

}
