package com.projectoao.rol;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.support.AutenticacionTestHelper;
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
 * Test de integracion del ciclo completo CRUD para el recurso rol.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RolCrudIntegrationTest {

	@LocalServerPort
	private int port;

	private WebTestClient client() {
		WebTestClient sinAutenticar = WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(10))
				.build();
		return AutenticacionTestHelper.clienteAutenticado(sinAutenticar, CredencialesAdmin.USERNAME,
				CredencialesAdmin.PASSWORD);
	}

	@Test
	void ciclCompletoCrudDeRol() {
		WebTestClient client = client();

		RolRequestDto nuevoRol = new RolRequestDto();
		nuevoRol.setNombre("ADMIN_TEST");
		nuevoRol.setDescripcion("Rol de prueba");

		RolDto creado = client.post().uri("/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoRol)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(RolDto.class)
				.returnResult().getResponseBody();

		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(creado.getNombre()).isEqualTo("ADMIN_TEST");
		assertThat(creado.getActivo()).isTrue();
		assertThat(creado.getFechaCreacion()).isNotNull();

		client.get().uri("/roles/nombre/admin_te")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(RolDto.class)
				.value(lista -> assertThat(lista).extracting(RolDto::getId).contains(creado.getId()));

		RolRequestDto actualizacion = new RolRequestDto();
		actualizacion.setNombre("ADMIN_TEST");
		actualizacion.setDescripcion("Descripcion actualizada");
		actualizacion.setActivo(false);

		client.put().uri("/roles/{id}", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(actualizacion)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.descripcion").isEqualTo("Descripcion actualizada")
				.jsonPath("$.activo").isEqualTo(false);

		client.get().uri("/roles")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(RolDto.class)
				.value(lista -> assertThat(lista).extracting(RolDto::getId).contains(creado.getId()));

		client.delete().uri("/roles/{id}", creado.getId())
				.exchange()
				.expectStatus().isNoContent();

		client.get().uri("/roles/nombre/ADMIN_TEST")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(RolDto.class)
				.value(lista -> assertThat(lista).extracting(RolDto::getId).doesNotContain(creado.getId()));
	}

}
