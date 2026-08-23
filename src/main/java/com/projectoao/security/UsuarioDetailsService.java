package com.projectoao.security;

import com.projectoao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Autentica contra la tabla {@code usuario} y otorga como autoridad el rol asignado
 * (por ejemplo {@code ROLE_ADMINISTRADOR}), usado por Spring Security para HTTP Basic.
 */
@Component
@RequiredArgsConstructor
public class UsuarioDetailsService implements ReactiveUserDetailsService {

	private final UsuarioRepository usuarioRepository;

	/** {@inheritDoc} */
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return Mono.fromCallable(() -> usuarioRepository.findByUsername(username))
				.subscribeOn(Schedulers.boundedElastic())
				.flatMap(Mono::justOrEmpty)
				.map(usuario -> User.withUsername(usuario.getUsername())
						.password(usuario.getPassword())
						.authorities("ROLE_" + usuario.getRol().getNombre().toUpperCase())
						.disabled(!Boolean.TRUE.equals(usuario.getActivo()))
						.build());
	}

}
