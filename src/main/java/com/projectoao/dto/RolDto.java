package com.projectoao.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un rol.
 */
@Data
public class RolDto {

	/** Identificador del rol. */
	private Long id;

	/** Nombre unico del rol. */
	private String nombre;

	/** Descripcion libre del rol. */
	private String descripcion;

	/** Indica si el rol esta activo. */
	private Boolean activo;

	/** Fecha y hora en que se creo el rol. */
	private LocalDateTime fechaCreacion;

}
