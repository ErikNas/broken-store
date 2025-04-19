package ru.eriknas.brokenstore.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Пользователь")
public class UserDTO {

    @Schema(hidden = true)
    private Integer id;

    @Schema(description = "Имя пользователя", example = "string")
    @NotBlank(message = "Не может быть пустым")
    @Pattern(message = "first_name: Имя пользователя не может содержать цифры, спец. символы и должно содержать не более 32 символов",
            regexp = "^[a-zA-Zа-яА-ЯЁё]{1,32}$")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "string")
    @NotBlank(message = "Не может быть пустым")
    @Pattern(message = "last_name: Фамилия пользователя не может содержать цифры, спец. символы и должно содержать не более 32 символов",
            regexp = "^[a-zA-Zа-яА-ЯЁё]{1,32}$")
    private String lastName;

    @Schema(description = "Номер телефона", example = "string")
    @Pattern(message = "phone: Номер телефона должен начинаться с 7 и иметь 11 цифр",regexp = "^7\\d{10}$")
    private String phone;

    @Schema(description = "Адрес проживания пользователя")
    private String address;

    @Schema(description = "Электронная почта", example = "string")
    @Email
    @NotNull(message = "email: Не может быть пустым")
    @Pattern(message = "email: Электронная почта должна состоять из двух частей, разделенных @:" +
            "левая часть — логин, имя пользователя - это текст, который может содержать буквы (A-Z, a-z) и цифры (0-9)," +
            " правая часть — доменное имя", regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Schema(description = "Пароль", example = "string")
    @Size(min = 8, max = 32)
    @NotNull(message = "password: Не может быть пустым")
    @Pattern(message = "password: Пароль должен содержать от 8 до 32 символов, как минимум одну букву, одну цифру и " +
            "один специальный символ", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#/?^&])[A-Za-z\\d@$!%*#/?^&]{3,}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Schema(description = "Роль пользователя")
    private String role;
}
