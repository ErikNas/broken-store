package ru.eriknas.brokenstore.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:core-security.properties")
public class OpenAPISecurityConfig {
    @Value("${keycloak.server-url}")
    String authServerUrl;

    @Value("${keycloak.realm}")
    String realm;

    private static final String OAUTH_SCHEME_NAME = "Keycloak";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme())
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(OAUTH_SCHEME_NAME)
                )
                .info(
                        new Info()
                                .title("Todos Management Service")
                                .description("A service providing todos.")
                                .version("1.0")
                );
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow flow = createAuthorizationCodeFlow();
        return new OAuthFlows()
                .password(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        return new OAuthFlow()
                .tokenUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .refreshUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .scopes(new Scopes());
    }
}
