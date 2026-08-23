package com.projectoao.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

/**
 * Verifica que Swagger/OpenAPI queda expuesto correctamente.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SwaggerConfigTest {

	@LocalServerPort
	private int port;

	private WebTestClient client() {
		return WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(10))
				.build();
	}

	@Test
	void elDocumentoOpenApiSeExponeCorrectamente() {
		client().get().uri("/v3/api-docs")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.info.title").isEqualTo("Proyecto A-O")
				.jsonPath("$.paths./roles").exists()
				.jsonPath("$.paths./usuarios").exists();
	}

	@Test
	void laInterfazDeSwaggerUiResponde() {
		client().get().uri("/swagger-ui.html")
				.exchange()
				.expectStatus().is3xxRedirection();
	}

}
