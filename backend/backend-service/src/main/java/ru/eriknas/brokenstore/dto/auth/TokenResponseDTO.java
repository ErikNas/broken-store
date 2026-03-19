package ru.eriknas.brokenstore.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ с токеном авторизации")
public class TokenResponseDTO {

    @Schema(description = "Access token для доступа к API")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Refresh token для обновления access token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Время жизни access token в секундах")
    @JsonProperty("expires_in")
    private int expiresIn;

    @Schema(description = "Тип токена", example = "Bearer")
    @JsonProperty("token_type")
    private String tokenType;
}
