package com.projectoao.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadata basica del documento OpenAPI expuesto por Swagger, incluyendo el esquema de
 * autenticacion Bearer JWT para poder probar los endpoints desde el boton "Authorize" (usando
 * el token que devuelve {@code POST /auth/login}).
 */
@Configuration
public class OpenApiConfig {

	private static final String ESQUEMA_BEARER_AUTH = "bearerAuth";

	@Bean
	public OpenAPI proyectoAOOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Proyecto A-O")
						.description("Microservicio reactivo de demostracion (WebFlux + JPA) - CRUD de roles y usuarios")
						.version("v1"))
				.components(new Components()
						.addSecuritySchemes(ESQUEMA_BEARER_AUTH, new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")))
				.addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BEARER_AUTH));
	}

}
