package com.projectoao.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Filtros opcionales para la busqueda de registros de almacenamiento. Todos los campos son
 * opcionales; el endpoint puede recibir todos, algunos o ninguno.
 */
@Data
public class AlmacenamientoFiltroDto {

	/** Coincidencia parcial (sin distinguir mayusculas) contra el objeto almacenado. */
	private String objetoAlmacenado;

	/** Fecha y hora exacta de ingreso a filtrar. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaIngreso;

	/** Fecha y hora exacta de salida a filtrar. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaSalida;

}
