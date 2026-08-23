package com.projectoao.repository;

import com.projectoao.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para {@link Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su username exacto.
	 *
	 * @param username username exacto del usuario
	 * @return el usuario encontrado, o vacio si no existe
	 */
	Optional<Usuario> findByUsername(String username);

}
