package com.projectoao.repository;

import com.projectoao.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para {@link Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Busca un usuario por su username exacto. Uso interno (autenticacion en
	 * {@code UsuarioDetailsService}); para el endpoint de busqueda publico ver
	 * {@link #findByUsernameContainingIgnoreCase(String)}.
	 *
	 * @param username username exacto del usuario
	 * @return el usuario encontrado, o vacio si no existe
	 */
	Optional<Usuario> findByUsername(String username);

	/**
	 * Busca usuarios cuyo username contenga el texto dado, sin distinguir mayusculas.
	 *
	 * @param username texto a buscar dentro del username
	 * @return los usuarios que coinciden
	 */
	List<Usuario> findByUsernameContainingIgnoreCase(String username);

}
