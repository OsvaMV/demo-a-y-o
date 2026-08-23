package com.projectoao.config;

import com.projectoao.security.JwtAuthenticationConverter;
import com.projectoao.security.JwtAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Configuracion de seguridad: autenticacion por token JWT (emitido en {@code POST /auth/login}
 * contra la tabla usuario) y autorizacion por rol (ADMINISTRADOR, GERENTE, TECNICO) sobre los
 * endpoints de roles, usuarios y almacenamiento.
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
	 * Define las reglas de autenticacion (JWT, sin estado) y autorizacion de la aplicacion.
	 *
	 * @param http constructor de configuracion de seguridad reactiva
	 * @param jwtAuthenticationManager valida el token y reconstruye la autenticacion
	 * @param jwtAuthenticationConverter extrae el token del header Authorization
	 * @return la cadena de filtros de seguridad configurada
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
			JwtAuthenticationManager jwtAuthenticationManager,
			JwtAuthenticationConverter jwtAuthenticationConverter) {

		AuthenticationWebFilter jwtWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
		jwtWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
		jwtWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

		return http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
						.permitAll()

						// Frontend estatico (html/css/js): publico. La proteccion real sigue
						// pasando por la API; estos archivos son solo marcado sin datos sensibles.
						.pathMatchers("/", "/*.html", "/css/**", "/js/**").permitAll()

						// Roles: ADMINISTRADOR y GERENTE pueden consultar (GERENTE necesita ver
						// los roles disponibles para asignarselos a los usuarios que crea);
						// crear, actualizar y eliminar sigue siendo unicamente ADMINISTRADOR
						.pathMatchers(HttpMethod.GET, "/roles/**").hasAnyRole("ADMINISTRADOR", "GERENTE")
						.pathMatchers(HttpMethod.POST, "/roles").hasRole("ADMINISTRADOR")
						.pathMatchers(HttpMethod.PUT, "/roles/**").hasRole("ADMINISTRADOR")
						.pathMatchers(HttpMethod.DELETE, "/roles/**").hasRole("ADMINISTRADOR")

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
				.addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.build();
	}

}
