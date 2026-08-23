package com.projectoao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para crear o actualizar un rol.
 */
@Data
public class RolRequestDto {

	/** Nombre unico del rol. */
	@NotBlank(message = "El nombre del rol es obligatorio")
	@Size(max = 50, message = "El nombre del rol no debe superar los 50 caracteres")
	private String nombre;

	/** Descripcion libre del rol. */
	@Size(max = 255, message = "La descripcion no debe superar los 255 caracteres")
	private String descripcion;

	/** Indica si el rol esta activo. */
	private Boolean activo;

}
