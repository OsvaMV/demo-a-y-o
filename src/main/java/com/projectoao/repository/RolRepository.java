package com.projectoao.repository;

import com.projectoao.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para {@link Rol}.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {

	/**
	 * Busca un rol por su nombre exacto.
	 *
	 * @param nombre nombre exacto del rol
	 * @return el rol encontrado, o vacio si no existe
	 */
	Optional<Rol> findByNombre(String nombre);

}
