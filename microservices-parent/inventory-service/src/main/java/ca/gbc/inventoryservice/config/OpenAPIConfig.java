package ca.gbc.inventoryservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//for documentation of our product service
@Configuration
public class OpenAPIConfig {
    @Value("${inventory-service.version}")
    private String verison;
    @Bean
    public OpenAPI productServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("O Service")
                        .description("This is the rest api for order service")
                        .version(verison)
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Product Service API - GBC - COMP3095 - 2024")
                        .url("https://mycompany.ca/order-service"));

    }
}

