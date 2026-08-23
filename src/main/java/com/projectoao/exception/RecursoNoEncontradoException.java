package com.projectoao.exception;

/**
 * Excepcion lanzada cuando un recurso solicitado (rol o usuario) no existe.
 */
public class RecursoNoEncontradoException extends RuntimeException {

	public RecursoNoEncontradoException(String mensaje) {
		super(mensaje);
	}

}
