package com.shopverse.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customAPI() {
		
		final String securitySchemeName = "bearerAuth";
		
		return new OpenAPI().info(new Info().title("Shopverse API").version("1.0")
				.description("A RESTful API for managing an e-commerce platform with products, orders, and users")
				.contact(new Contact().name("Jonnel Fernandez")
										.email("fernandezjonnel@yahoo.com")
					)
				)
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
						.name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}
