package com.projectoao.repository;

import com.projectoao.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para {@link Rol}.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {

	/**
	 * Busca un rol por su nombre exacto. Uso interno (p. ej. sembrado del rol
	 * ADMINISTRADOR); para el endpoint de busqueda publico ver
	 * {@link #findByNombreContainingIgnoreCase(String)}.
	 *
	 * @param nombre nombre exacto del rol
	 * @return el rol encontrado, o vacio si no existe
	 */
	Optional<Rol> findByNombre(String nombre);

	/**
	 * Busca roles cuyo nombre contenga el texto dado, sin distinguir mayusculas.
	 *
	 * @param nombre texto a buscar dentro del nombre del rol
	 * @return los roles que coinciden
	 */
	List<Rol> findByNombreContainingIgnoreCase(String nombre);

}
