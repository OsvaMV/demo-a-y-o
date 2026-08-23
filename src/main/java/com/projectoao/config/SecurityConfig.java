package com.projectoao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuracion de seguridad: autenticacion HTTP Basic contra la tabla usuario y autorizacion
 * por rol (ADMINISTRADOR, GERENTE, TECNICO) sobre los endpoints de roles, usuarios y
 * almacenamiento.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	/**
	 * Codificador de contrasenas usado tanto al autenticar como al guardar usuarios.
	 *
	 * @return un {@link BCryptPasswordEncoder}
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Define las reglas de autenticacion y autorizacion de la aplicacion.
	 *
	 * @param http constructor de configuracion de seguridad reactiva
	 * @return la cadena de filtros de seguridad configurada
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(Customizer.withDefaults())
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
						.permitAll()

						// Roles: unicamente ADMINISTRADOR
						.pathMatchers("/roles/**").hasRole("ADMINISTRADOR")

						// Usuarios: crear y consultar -> ADMINISTRADOR y GERENTE
						// actualizar y eliminar -> unicamente ADMINISTRADOR
						.pathMatchers(HttpMethod.POST, "/usuarios").hasAnyRole("ADMINISTRADOR", "GERENTE")
						.pathMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
						.pathMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("ADMINISTRADOR")
						.pathMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMINISTRADOR")

						// Almacenamiento: consultar y agregar -> los 3 roles
						// actualizar y eliminar -> ADMINISTRADOR y GERENTE (TECNICO no)
						.pathMatchers(HttpMethod.GET, "/almacenamientos")
						.hasAnyRole("ADMINISTRADOR", "GERENTE", "TECNICO")
						.pathMatchers(HttpMethod.POST, "/almacenamientos")
						.hasAnyRole("ADMINISTRADOR", "GERENTE", "TECNICO")
						.pathMatchers(HttpMethod.PUT, "/almacenamientos/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
						.pathMatchers(HttpMethod.DELETE, "/almacenamientos/**").hasAnyRole("ADMINISTRADOR", "GERENTE")

						.anyExchange().authenticated())
				.build();
	}

}
