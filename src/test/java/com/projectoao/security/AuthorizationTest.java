package com.projectoao.security;

import com.projectoao.dto.AlmacenamientoDto;
import com.projectoao.dto.AlmacenamientoRequestDto;
import com.projectoao.dto.RolDto;
import com.projectoao.dto.RolRequestDto;
import com.projectoao.dto.UsuarioDto;
import com.projectoao.dto.UsuarioRequestDto;
import com.projectoao.support.AutenticacionTestHelper;
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
 * Verifica la matriz de permisos por rol: ADMINISTRADOR (acceso total), GERENTE
 * (roles solo consultar; usuarios solo crear/consultar; almacenamiento total) y TECNICO
 * (sin roles ni usuarios; almacenamiento solo consultar/agregar).
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthorizationTest {

	@LocalServerPort
	private int port;

	private WebTestClient rawClient() {
		return WebTestClient.bindToServer()
				.baseUrl("http://localhost:" + port)
				.responseTimeout(Duration.ofSeconds(10))
				.build();
	}

	private WebTestClient clienteAutenticado(String username, String password) {
		return AutenticacionTestHelper.clienteAutenticado(rawClient(), username, password);
	}

	private WebTestClient clienteAdmin() {
		return clienteAutenticado(CredencialesAdmin.USERNAME, CredencialesAdmin.PASSWORD);
	}

	private Long crearRol(String nombre) {
		RolRequestDto request = new RolRequestDto();
		request.setNombre(nombre);

		RolDto rol = clienteAdmin().post().uri("/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(RolDto.class)
				.returnResult().getResponseBody();

		assertThat(rol).isNotNull();
		return rol.getId();
	}

	private void crearUsuario(String username, String password, Long rolId) {
		UsuarioRequestDto request = new UsuarioRequestDto();
		request.setUsername(username);
		request.setEmail(username + "@example.com");
		request.setPassword(password);
		request.setRolId(rolId);

		clienteAdmin().post().uri("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated();
	}

	@Test
	void sinCredencialesDevuelve401() {
		rawClient().get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void gerentePuedeConsultarRolesYCrearConsultarUsuariosPeroNoActualizarNiEliminarUsuariosNiTocarRolesMasAlla_yTieneAccesoTotalAAlmacenamiento() {
		Long rolGerenteId = crearRol("gerente");
		crearUsuario("gerente_auth", "Gerente123!", rolGerenteId);
		WebTestClient gerente = clienteAutenticado("gerente_auth", "Gerente123!");

		gerente.get().uri("/roles")
				.exchange()
				.expectStatus().isOk();
		gerente.get().uri("/roles/nombre/gerente")
				.exchange()
				.expectStatus().isOk();
		gerente.post().uri("/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new RolRequestDto())
				.exchange()
				.expectStatus().isForbidden();
		gerente.put().uri("/roles/{id}", rolGerenteId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new RolRequestDto())
				.exchange()
				.expectStatus().isForbidden();
		gerente.delete().uri("/roles/{id}", rolGerenteId)
				.exchange()
				.expectStatus().isForbidden();

		UsuarioRequestDto nuevoUsuario = new UsuarioRequestDto();
		nuevoUsuario.setUsername("creado_por_gerente");
		nuevoUsuario.setEmail("creado_por_gerente@example.com");
		nuevoUsuario.setPassword("Clave123!");
		nuevoUsuario.setRolId(rolGerenteId);

		UsuarioDto usuarioCreado = gerente.post().uri("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoUsuario)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(UsuarioDto.class)
				.returnResult().getResponseBody();
		assertThat(usuarioCreado).isNotNull();

		gerente.get().uri("/usuarios")
				.exchange()
				.expectStatus().isOk();

		UsuarioRequestDto actualizacion = new UsuarioRequestDto();
		actualizacion.setUsername("creado_por_gerente");
		actualizacion.setEmail("otro@example.com");
		actualizacion.setPassword("Clave123!");
		actualizacion.setRolId(rolGerenteId);

		gerente.put().uri("/usuarios/{id}", usuarioCreado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(actualizacion)
				.exchange()
				.expectStatus().isForbidden();

		gerente.delete().uri("/usuarios/{id}", usuarioCreado.getId())
				.exchange()
				.expectStatus().isForbidden();

		LocalDateTime ahora = LocalDateTime.now().withNano(0);
		AlmacenamientoRequestDto nuevoAlmacenamiento = new AlmacenamientoRequestDto();
		nuevoAlmacenamiento.setObjetoAlmacenado("Caja de pruebas gerente");
		nuevoAlmacenamiento.setFechaIngreso(ahora);

		Long almacenamientoId = gerente.post().uri("/almacenamientos")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoAlmacenamiento)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(AlmacenamientoDto.class)
				.returnResult().getResponseBody().getId();

		gerente.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isOk();

		nuevoAlmacenamiento.setFechaSalida(ahora.plusDays(1));
		gerente.put().uri("/almacenamientos/{id}", almacenamientoId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoAlmacenamiento)
				.exchange()
				.expectStatus().isOk();

		gerente.delete().uri("/almacenamientos/{id}", almacenamientoId)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	void tecnicoNoPuedeTocarRolesNiUsuarios_soloConsultaYAgregaEnAlmacenamiento() {
		Long rolTecnicoId = crearRol("tecnico");
		crearUsuario("tecnico_auth", "Tecnico123!", rolTecnicoId);
		WebTestClient tecnico = clienteAutenticado("tecnico_auth", "Tecnico123!");

		tecnico.get().uri("/roles")
				.exchange()
				.expectStatus().isForbidden();

		tecnico.get().uri("/usuarios")
				.exchange()
				.expectStatus().isForbidden();
		tecnico.post().uri("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new UsuarioRequestDto())
				.exchange()
				.expectStatus().isForbidden();

		LocalDateTime ahora = LocalDateTime.now().withNano(0);
		AlmacenamientoRequestDto nuevoAlmacenamiento = new AlmacenamientoRequestDto();
		nuevoAlmacenamiento.setObjetoAlmacenado("Caja de pruebas tecnico");
		nuevoAlmacenamiento.setFechaIngreso(ahora);

		Long almacenamientoId = tecnico.post().uri("/almacenamientos")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoAlmacenamiento)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(AlmacenamientoDto.class)
				.returnResult().getResponseBody().getId();

		tecnico.get().uri("/almacenamientos")
				.exchange()
				.expectStatus().isOk();

		nuevoAlmacenamiento.setFechaSalida(ahora.plusDays(1));
		tecnico.put().uri("/almacenamientos/{id}", almacenamientoId)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(nuevoAlmacenamiento)
				.exchange()
				.expectStatus().isForbidden();

		tecnico.delete().uri("/almacenamientos/{id}", almacenamientoId)
				.exchange()
				.expectStatus().isForbidden();
	}

}
