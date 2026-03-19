package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.eriknas.brokenstore.dto.auth.LoginRequestDTO;
import ru.eriknas.brokenstore.dto.auth.TokenResponseDTO;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Авторизация")
public class AuthController {

    private final String tokenUrl;
    private final String clientId;
    private final String clientSecret;
    private final RestClient restClient;

    public AuthController(
            @Value("${keycloak.server-url}") String serverUrl,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret) {
        this.tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = RestClient.create();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Авторизоваться по логину и паролю",
            description = "Возвращает access token и refresh token. " +
                    "Используйте access_token в заголовке: Authorization: Bearer <token>",
            security = {}
    )
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        try {
            TokenResponseDTO token = restClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(TokenResponseDTO.class);

            return ResponseEntity.ok(token);
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getResponseBodyAsString()));
        }
    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить токен текущего пользователя",
            description = "Возвращает access token авторизованного пользователя. " +
                    "Требует авторизацию (через Swagger UI или Bearer token)"
    )
    public ResponseEntity<Map<String, String>> getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        return ResponseEntity.ok(Map.of(
                "access_token", token,
                "token_type", "Bearer"
        ));
    }
}
