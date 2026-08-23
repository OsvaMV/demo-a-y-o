package com.projectoao.usuario;

import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
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
 * Test de integracion del ciclo completo CRUD para el recurso usuario.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UsuarioCrudIntegrationTest {

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
	void ciclCompletoCrudDeUsuario() {
		WebTestClient client = client();

		RolRequestDto nuevoRol = new RolRequestDto();
		nuevoRol.setNombre("USER_TEST");
		nuevoRol.setDescripcion("Rol para pruebas de usuario");

		RolDto rol = client.post().uri("/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoRol)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(RolDto.class)
				.returnResult().getResponseBody();

		assertThat(rol).isNotNull();

		UsuarioRequestDto nuevoUsuario = new UsuarioRequestDto();
		nuevoUsuario.setUsername("jperez");
		nuevoUsuario.setEmail("jperez@example.com");
		nuevoUsuario.setPassword("clave-segura");
		nuevoUsuario.setNombre("Juan");
		nuevoUsuario.setApellido("Perez");
		nuevoUsuario.setRolId(rol.getId());

		UsuarioDto creado = client.post().uri("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoUsuario)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(UsuarioDto.class)
				.returnResult().getResponseBody();

		assertThat(creado).isNotNull();
		assertThat(creado.getId()).isNotNull();
		assertThat(creado.getUsername()).isEqualTo("jperez");
		assertThat(creado.getActivo()).isTrue();
		assertThat(creado.getRol()).isNotNull();
		assertThat(creado.getRol().getId()).isEqualTo(rol.getId());

		client.get().uri("/usuarios/username/jperez")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(creado.getId().intValue())
				.jsonPath("$.rol.nombre").isEqualTo("USER_TEST");

		UsuarioRequestDto actualizacion = new UsuarioRequestDto();
		actualizacion.setUsername("jperez");
		actualizacion.setEmail("jperez.nuevo@example.com");
		actualizacion.setPassword("clave-segura");
		actualizacion.setNombre("Juan");
		actualizacion.setApellido("Perez Actualizado");
		actualizacion.setActivo(false);
		actualizacion.setRolId(rol.getId());

		client.put().uri("/usuarios/{id}", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(actualizacion)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.email").isEqualTo("jperez.nuevo@example.com")
				.jsonPath("$.apellido").isEqualTo("Perez Actualizado")
				.jsonPath("$.activo").isEqualTo(false);

		client.get().uri("/usuarios")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(UsuarioDto.class)
				.value(lista -> assertThat(lista).extracting(UsuarioDto::getId).contains(creado.getId()));

		client.delete().uri("/usuarios/{id}", creado.getId())
				.exchange()
				.expectStatus().isNoContent();

		client.get().uri("/usuarios/username/jperez")
				.exchange()
				.expectStatus().isNotFound();
	}

}
