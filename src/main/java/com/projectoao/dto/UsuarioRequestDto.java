package com.projectoao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para crear o actualizar un usuario.
 */
@Data
public class UsuarioRequestDto {

	/** Nombre de usuario, unico. */
	@NotBlank(message = "El username es obligatorio")
	@Size(max = 50, message = "El username no debe superar los 50 caracteres")
	private String username;

	/** Correo electronico, unico. */
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email no tiene un formato valido")
	@Size(max = 150, message = "El email no debe superar los 150 caracteres")
	private String email;

	/** Contrasena del usuario. */
	@NotBlank(message = "La contrasena es obligatoria")
	@Size(max = 255, message = "La contrasena no debe superar los 255 caracteres")
	private String password;

	/** Nombre de pila del usuario. */
	@Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
	private String nombre;

	/** Apellido del usuario. */
	@Size(max = 100, message = "El apellido no debe superar los 100 caracteres")
	private String apellido;

	/** Indica si el usuario esta activo. */
	private Boolean activo;

	/** Identificador del rol asignado al usuario. */
	@NotNull(message = "El rol es obligatorio")
	private Long rolId;

}
