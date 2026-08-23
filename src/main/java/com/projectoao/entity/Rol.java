package com.projectoao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla {@code rol}.
 */
@Getter
@Setter
@Entity
@Table(name = "rol")
public class Rol {

	/** Identificador autogenerado del rol. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Nombre unico del rol. */
	@Column(name = "nombre", nullable = false, unique = true, length = 50)
	private String nombre;

	/** Descripcion libre del rol. */
	@Column(name = "descripcion", length = 255)
	private String descripcion;

	/** Indica si el rol esta activo. */
	@Column(name = "activo", nullable = false)
	private Boolean activo = Boolean.TRUE;

	/** Fecha y hora en que se creo el rol. */
	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	@PrePersist
	protected void alPersistir() {
		if (fechaCreacion == null) {
			fechaCreacion = LocalDateTime.now();
		}
		if (activo == null) {
			activo = Boolean.TRUE;
		}
	}

}
