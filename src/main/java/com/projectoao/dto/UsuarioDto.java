package com.projectoao.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un usuario, incluye el {@link RolDto} anidado. Nunca expone la contrasena.
 */
@Data
public class UsuarioDto {

	/** Identificador del usuario. */
	private Long id;

	/** Nombre de usuario, unico. */
	private String username;

	/** Correo electronico, unico. */
	private String email;

	/** Nombre de pila del usuario. */
	private String nombre;

	/** Apellido del usuario. */
	private String apellido;

	/** Indica si el usuario esta activo. */
	private Boolean activo;

	/** Fecha y hora en que se creo el usuario. */
	private LocalDateTime fechaCreacion;

	/** Rol asignado al usuario. */
	private RolDto rol;

}
