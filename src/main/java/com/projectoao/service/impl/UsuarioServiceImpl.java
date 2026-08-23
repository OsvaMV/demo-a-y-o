package com.projectoao.service.impl;

import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
import com.projectoao.entity.Rol;
import com.projectoao.entity.Usuario;
import com.projectoao.exception.RecursoNoEncontradoException;
import com.projectoao.mapper.UsuarioMapper;
import com.projectoao.repository.RolRepository;
import com.projectoao.repository.UsuarioRepository;
import com.projectoao.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementacion de {@link UsuarioService}. Envuelve las llamadas JPA (bloqueantes) en
 * {@link Schedulers#boundedElastic()} para no bloquear los hilos reactivos de Netty.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final RolRepository rolRepository;
	private final UsuarioMapper usuarioMapper;
	private final PasswordEncoder passwordEncoder;

	/** {@inheritDoc} */
	@Override
	public Mono<UsuarioDto> guardar(UsuarioRequestDto request) {
		return Mono.fromCallable(() -> {
					Rol rol = buscarRolOFallar(request.getRolId());
					Usuario usuario = usuarioMapper.toEntity(request);
					usuario.setPassword(passwordEncoder.encode(request.getPassword()));
					usuario.setRol(rol);
					return usuarioMapper.toDto(usuarioRepository.save(usuario));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<UsuarioDto> actualizar(Long id, UsuarioRequestDto request) {
		return Mono.fromCallable(() -> {
					Usuario usuario = usuarioRepository.findById(id)
							.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
					Rol rol = buscarRolOFallar(request.getRolId());
					usuarioMapper.actualizarEntidadDesdeDto(request, usuario);
					usuario.setPassword(passwordEncoder.encode(request.getPassword()));
					usuario.setRol(rol);
					return usuarioMapper.toDto(usuarioRepository.save(usuario));
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<Void> eliminar(Long id) {
		return Mono.<Void>fromRunnable(() -> {
					if (!usuarioRepository.existsById(id)) {
						throw new RecursoNoEncontradoException("Usuario no encontrado con id " + id);
					}
					usuarioRepository.deleteById(id);
				})
				.subscribeOn(Schedulers.boundedElastic());
	}

	/** {@inheritDoc} */
	@Override
	public Flux<UsuarioDto> buscarTodos() {
		return Mono.fromCallable(usuarioRepository::findAll)
				.subscribeOn(Schedulers.boundedElastic())
				.flatMapMany(Flux::fromIterable)
				.map(usuarioMapper::toDto);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<UsuarioDto> buscarPorUsername(String username) {
		return Mono.fromCallable(() -> usuarioRepository.findByUsername(username)
						.map(usuarioMapper::toDto)
						.orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con username " + username)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	private Rol buscarRolOFallar(Long rolId) {
		return rolRepository.findById(rolId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id " + rolId));
	}

}
