package com.projectoao.repository;

import com.projectoao.entity.Almacenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio JPA para {@link Almacenamiento}. Extiende {@link JpaSpecificationExecutor} para
 * soportar la busqueda con filtros dinamicos opcionales.
 */
public interface AlmacenamientoRepository
		extends JpaRepository<Almacenamiento, Long>, JpaSpecificationExecutor<Almacenamiento> {

}
