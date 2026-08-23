package com.projectoao.support;

import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Hace login contra {@code POST /auth/login} en los tests de integracion y arma un
 * {@link WebTestClient} que ya manda el token obtenido como {@code Authorization: Bearer}.
 */
public final class AutenticacionTestHelper {

	private AutenticacionTestHelper() {
	}

	/**
	 * Hace login y devuelve un cliente que reenvia el token en cada request.
	 *
	 * @param clienteSinAutenticar cliente base, sin credenciales
	 * @param username username a autenticar
	 * @param password password del usuario
	 * @return un nuevo {@link WebTestClient} con el header Authorization ya configurado
	 */
	public static WebTestClient clienteAutenticado(WebTestClient clienteSinAutenticar, String username,
			String password) {
		String token = obtenerToken(clienteSinAutenticar, username, password);
		return clienteSinAutenticar.mutate()
				.defaultHeaders(headers -> headers.setBearerAuth(token))
				.build();
	}

	/**
	 * Hace login y devuelve unicamente el token emitido.
	 *
	 * @param clienteSinAutenticar cliente base, sin credenciales
	 * @param username username a autenticar
	 * @param password password del usuario
	 * @return el token JWT emitido
	 */
	public static String obtenerToken(WebTestClient clienteSinAutenticar, String username, String password) {
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername(username);
		request.setPassword(password);

		LoginResponseDto response = clienteSinAutenticar.post().uri("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isOk()
				.expectBody(LoginResponseDto.class)
				.returnResult().getResponseBody();

		if (response == null || response.getToken() == null) {
			throw new IllegalStateException("No se pudo obtener el token para " + username);
		}
		return response.getToken();
	}

}
