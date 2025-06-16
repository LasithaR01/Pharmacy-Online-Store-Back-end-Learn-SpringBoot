package pharmacy.pharmacy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        // Define tags in the order you want them to appear
        List<Tag> tags = Arrays.asList(
                new Tag().name("Authentication").extensions(java.util.Map.of("x-order", 16)),
                new Tag().name("User Management").extensions(java.util.Map.of("x-order", 15)),
                new Tag().name("Branch Management").extensions(java.util.Map.of("x-order", 3)),
                new Tag().name("Product Management").extensions(java.util.Map.of("x-order", 4)),
                new Tag().name("Category Management").extensions(java.util.Map.of("x-order", 5)),
                new Tag().name("Inventory Management").extensions(java.util.Map.of("x-order", 6)),
                new Tag().name("Supplier Management").extensions(java.util.Map.of("x-order", 7)),
                new Tag().name("Stock Management").extensions(java.util.Map.of("x-order", 8)),
                new Tag().name("Order Management").extensions(java.util.Map.of("x-order", 1)),
                new Tag().name("Order Item Management").extensions(java.util.Map.of("x-order", 2)),
                new Tag().name("Prescription Management").extensions(java.util.Map.of("x-order", 9)),
                new Tag().name("Notification Management").extensions(java.util.Map.of("x-order", 10)),
                new Tag().name("Alert Management").extensions(java.util.Map.of("x-order", 11)),
                new Tag().name("Restock Request Management").extensions(java.util.Map.of("x-order", 12)),
                new Tag().name("Product Alternatives Management").extensions(java.util.Map.of("x-order", 13)),
                new Tag().name("Drug Interaction Management").extensions(java.util.Map.of("x-order", 14))
        );

        return new OpenAPI()
                .info(new Info()
                        .title("Sethma Pharmacy API Documentation")
                        .version("1.0")
                        .description("Spring Boot REST API with JWT Authentication"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .tags(tags);
    }
}