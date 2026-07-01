package com.ecommerce.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Distributed E-commerce API Gateway",
                version = "1.0",
                description = "Documentation for the Gateway and routing to downstream microservices."
        )
)
public class SwaggerConfig {
}
