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
 * autenticacion HTTP Basic para poder probar los endpoints desde el boton "Authorize".
 */
@Configuration
public class OpenApiConfig {

	private static final String ESQUEMA_BASIC_AUTH = "basicAuth";

	@Bean
	public OpenAPI proyectoAOOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Proyecto A-O")
						.description("Microservicio reactivo de demostracion (WebFlux + JPA) - CRUD de roles y usuarios")
						.version("v1"))
				.components(new Components()
						.addSecuritySchemes(ESQUEMA_BASIC_AUTH, new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("basic")))
				.addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BASIC_AUTH));
	}

}
