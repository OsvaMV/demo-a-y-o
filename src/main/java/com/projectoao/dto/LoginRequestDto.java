package com.projectoao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de entrada para autenticarse y obtener un token JWT.
 */
@Data
public class LoginRequestDto {

	/** Username del usuario. */
	@NotBlank(message = "El username es obligatorio")
	private String username;

	/** Contrasena del usuario. */
	@NotBlank(message = "La contrasena es obligatoria")
	private String password;

}
