package com.projectoao.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para un registro de almacenamiento.
 */
@Data
public class AlmacenamientoDto {

	/** Identificador del registro. */
	private Long id;

	/** Descripcion del objeto almacenado. */
	private String objetoAlmacenado;

	/** Fecha y hora en que el objeto ingreso al almacen. */
	private LocalDateTime fechaIngreso;

	/** Fecha y hora en que el objeto salio del almacen (nulo si sigue almacenado). */
	private LocalDateTime fechaSalida;

}
