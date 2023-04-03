package com.mslfox.cloudStorageServices.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Authentication API and Storage Files Manager API",
        description = "MSL Group production",
        version = "1.0.0"))
@Configuration
public class OpenApiConfig {
    // may be i`ll do something later
}