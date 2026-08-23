package com.projectoao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de entrada para crear o actualizar un registro de almacenamiento.
 */
@Data
public class AlmacenamientoRequestDto {

	/** Descripcion del objeto almacenado. */
	@NotBlank(message = "El objeto almacenado es obligatorio")
	@Size(max = 150, message = "El objeto almacenado no debe superar los 150 caracteres")
	private String objetoAlmacenado;

	/** Fecha y hora en que el objeto ingreso al almacen. */
	@NotNull(message = "La fecha de ingreso es obligatoria")
	private LocalDateTime fechaIngreso;

	/** Fecha y hora en que el objeto salio del almacen (opcional, mientras siga almacenado). */
	private LocalDateTime fechaSalida;

}
