package com.projectoao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla {@code usuario}. Cada usuario pertenece a un {@link Rol}.
 */
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

	/** Identificador autogenerado del usuario. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Nombre de usuario, unico. */
	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	/** Correo electronico, unico. */
	@Column(name = "email", nullable = false, unique = true, length = 150)
	private String email;

	/** Contrasena del usuario. */
	@Column(name = "password", nullable = false, length = 255)
	private String password;

	/** Nombre de pila del usuario. */
	@Column(name = "nombre", length = 100)
	private String nombre;

	/** Apellido del usuario. */
	@Column(name = "apellido", length = 100)
	private String apellido;

	/** Indica si el usuario esta activo. */
	@Column(name = "activo", nullable = false)
	private Boolean activo = Boolean.TRUE;

	/** Fecha y hora en que se creo el usuario. */
	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion;

	/**
	 * Rol asignado al usuario.
	 *
	 * <p>EAGER: el DTO de respuesta siempre incluye el rol anidado, y evita
	 * LazyInitializationException al mapear fuera de la sesion de Hibernate
	 * (findAll/findByUsername no dejan la sesion abierta hasta la capa de mapeo).
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rol_id", nullable = false)
	private Rol rol;

	@PrePersist
	protected void alPersistir() {
		if (fechaCreacion == null) {
			fechaCreacion = LocalDateTime.now();
		}
		if (activo == null) {
			activo = Boolean.TRUE;
		}
	}

}
