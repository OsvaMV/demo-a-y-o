package com.projectoao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla {@code almacenamiento}.
 */
@Getter
@Setter
@Entity
@Table(name = "almacenamiento")
public class Almacenamiento {

	/** Identificador autogenerado del registro de almacenamiento. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Descripcion del objeto almacenado. */
	@Column(name = "objeto_almacenado", nullable = false, length = 150)
	private String objetoAlmacenado;

	/** Fecha y hora en que el objeto ingreso al almacen. */
	@Column(name = "fecha_ingreso", nullable = false)
	private LocalDateTime fechaIngreso;

	/** Fecha y hora en que el objeto salio del almacen (nulo mientras siga almacenado). */
	@Column(name = "fecha_salida")
	private LocalDateTime fechaSalida;

}
