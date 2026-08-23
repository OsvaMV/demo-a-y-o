package com.projectoao.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Filtros opcionales para la busqueda de registros de almacenamiento. Todos los campos son
 * opcionales; el endpoint puede recibir todos, algunos o ninguno. Las fechas filtran por rango
 * (desde/hasta), no por coincidencia exacta.
 */
@Data
public class AlmacenamientoFiltroDto {

	/** Coincidencia parcial (sin distinguir mayusculas) contra el objeto almacenado. */
	private String objetoAlmacenado;

	/** Limite inferior (inclusive) del rango de fecha de ingreso. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaIngresoDesde;

	/** Limite superior (inclusive) del rango de fecha de ingreso. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaIngresoHasta;

	/** Limite inferior (inclusive) del rango de fecha de salida. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaSalidaDesde;

	/** Limite superior (inclusive) del rango de fecha de salida. */
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime fechaSalidaHasta;

}
