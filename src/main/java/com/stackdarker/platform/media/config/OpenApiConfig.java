package com.stackdarker.platform.media.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mediaServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Platform Media Service")
                        .description("Media service for file uploads, metadata, and download URLs via MinIO")
                        .version("1.0.0"))
                .schemaRequirement("bearer-jwt", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }
}
