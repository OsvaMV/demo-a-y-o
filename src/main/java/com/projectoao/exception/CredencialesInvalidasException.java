package com.projectoao.exception;

/**
 * Excepcion lanzada cuando el login recibe un username o password incorrectos.
 */
public class CredencialesInvalidasException extends RuntimeException {

	public CredencialesInvalidasException(String mensaje) {
		super(mensaje);
	}

}
