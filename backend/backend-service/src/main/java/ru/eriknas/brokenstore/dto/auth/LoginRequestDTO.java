package ru.eriknas.brokenstore.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на авторизацию")
public class LoginRequestDTO {

    @Schema(description = "Имя пользователя", example = "admin123")
    @NotBlank(message = "Укажите имя пользователя")
    private String username;

    @Schema(description = "Пароль", example = "admin123")
    @NotBlank(message = "Укажите пароль")
    private String password;
}
