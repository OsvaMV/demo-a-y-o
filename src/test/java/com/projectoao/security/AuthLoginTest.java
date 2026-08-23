package com.projectoao.security;

import com.projectoao.dto.LoginRequestDto;
import com.projectoao.dto.LoginResponseDto;
import com.projectoao.support.CredencialesAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica el flujo de login: emision del token JWT con credenciales validas, rechazo con
 * credenciales invalidas, y que el token emitido efectivamente sirve para llamar un endpoint
 * protegido.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthLoginTest {

	@LocalServerPort
	private int port;

	private WebTestClient client() {
		return WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(10))
				.build();
	}

	@Test
	void loginConCredencialesValidasDevuelveTokenYRol() {
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername(CredencialesAdmin.USERNAME);
		request.setPassword(CredencialesAdmin.PASSWORD);

		LoginResponseDto response = client().post().uri("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isOk()
				.expectBody(LoginResponseDto.class)
				.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getToken()).isNotBlank();
		assertThat(response.getTokenType()).isEqualTo("Bearer");
		assertThat(response.getUsername()).isEqualTo(CredencialesAdmin.USERNAME);
		assertThat(response.getRol()).isEqualTo("ADMINISTRADOR");
		assertThat(response.getExpiraEnMinutos()).isPositive();
	}

	@Test
	void loginConPasswordIncorrectaDevuelve401() {
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername(CredencialesAdmin.USERNAME);
		request.setPassword("password-incorrecta");

		client().post().uri("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void loginConUsuarioInexistenteDevuelve401() {
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername("no_existe");
		request.setPassword("cualquiera");

		client().post().uri("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void elTokenEmitidoPermiteAccederAUnEndpointProtegido() {
		LoginRequestDto request = new LoginRequestDto();
		request.setUsername(CredencialesAdmin.USERNAME);
		request.setPassword(CredencialesAdmin.PASSWORD);

		LoginResponseDto response = client().post().uri("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isOk()
				.expectBody(LoginResponseDto.class)
				.returnResult().getResponseBody();

		assertThat(response).isNotNull();

		client().mutate()
				.defaultHeaders(headers -> headers.setBearerAuth(response.getToken()))
				.build()
				.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void unTokenInvalidoDevuelve401() {
		client().mutate()
				.defaultHeaders(headers -> headers.setBearerAuth("esto-no-es-un-token-valido"))
				.build()
				.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isUnauthorized();
	}

}
