package com.projectoao.almacenamiento;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import com.projectoao.support.CredencialesAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de integracion del ciclo completo CRUD para el recurso almacenamiento, incluyendo la
 * busqueda por filtros opcionales combinables.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AlmacenamientoCrudIntegrationTest {

	@LocalServerPort
	private int port;

	private WebTestClient client() {
		return WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(10))
				.build()
				.mutate()
				.defaultHeaders(headers -> headers.setBasicAuth(CredencialesAdmin.USERNAME, CredencialesAdmin.PASSWORD))
				.build();
	}

	@Test
	void ciclCompletoCrudDeAlmacenamiento() {
		WebTestClient client = client();
		LocalDateTime fechaIngreso = LocalDateTime.of(2026, 1, 10, 8, 0, 0);

		AlmacenamientoRequestDto nuevo = new AlmacenamientoRequestDto();
		nuevo.setObjetoAlmacenado("Herramientas Electricas");
		nuevo.setFechaIngreso(fechaIngreso);

		AlmacenamientoDto creado = client.post().uri("/almacenamientos")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevo)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(AlmacenamientoDto.class)
				.returnResult().getResponseBody();

		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(creado.getObjetoAlmacenado()).isEqualTo("Herramientas Electricas");
		assertThat(creado.getFechaIngreso()).isEqualTo(fechaIngreso);
		assertThat(creado.getFechaSalida()).isNull();

		client.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(AlmacenamientoDto.class)
				.value(lista -> assertThat(lista).extracting(AlmacenamientoDto::getId).contains(creado.getId()));

		client.get().uri(uriBuilder -> uriBuilder.path("/almacenamientos")
						.queryParam("objetoAlmacenado", "herramientas")
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(AlmacenamientoDto.class)
				.value(lista -> assertThat(lista).extracting(AlmacenamientoDto::getId).contains(creado.getId()));

		client.get().uri(uriBuilder -> uriBuilder.path("/almacenamientos")
						.queryParam("objetoAlmacenado", "no-existe-esto")
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(AlmacenamientoDto.class)
				.value(lista -> assertThat(lista).extracting(AlmacenamientoDto::getId).doesNotContain(creado.getId()));

		LocalDateTime fechaSalida = fechaIngreso.plusDays(5);
		AlmacenamientoRequestDto actualizacion = new AlmacenamientoRequestDto();
		actualizacion.setObjetoAlmacenado("Herramientas Electricas");
		actualizacion.setFechaIngreso(fechaIngreso);
		actualizacion.setFechaSalida(fechaSalida);

		client.put().uri("/almacenamientos/{id}", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(actualizacion)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.fechaSalida").isNotEmpty();

		client.get().uri(uriBuilder -> uriBuilder.path("/almacenamientos")
						.queryParam("fechaSalida", fechaSalida.toString())
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(AlmacenamientoDto.class)
				.value(lista -> assertThat(lista).extracting(AlmacenamientoDto::getId).contains(creado.getId()));

		client.delete().uri("/almacenamientos/{id}", creado.getId())
				.exchange()
				.expectStatus().isNoContent();

		client.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(AlmacenamientoDto.class)
				.value(lista -> assertThat(lista).extracting(AlmacenamientoDto::getId).doesNotContain(creado.getId()));
	}

}
