package ru.eriknas.brokenstore.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:core-security.properties")
public class OpenAPISecurityConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Broken Store API")
                                .description("Песочница для тестирования. " +
                                        "Авторизация выполняется автоматически через Keycloak — " +
                                        "дополнительных действий в Swagger не требуется.")
                                .version("1.0")
                );
    }
}
