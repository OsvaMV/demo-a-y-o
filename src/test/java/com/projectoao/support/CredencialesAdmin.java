package com.projectoao.support;

/**
 * Credenciales del usuario administrador sembrado por {@code DataSeeder} al arrancar la
 * aplicacion, para reutilizar en los tests de integracion que requieren autenticacion.
 */
public final class CredencialesAdmin {

	/** Username del usuario administrador sembrado al arrancar. */
	public static final String USERNAME = "admin";

	/** Password (sin encriptar) del usuario administrador sembrado al arrancar. */
	public static final String PASSWORD = "Admin123!";

	private CredencialesAdmin() {
	}

}
