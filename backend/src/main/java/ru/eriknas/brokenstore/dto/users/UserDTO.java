package ru.eriknas.brokenstore.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//@Entity
//@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@Schema(description = "Пользователь")
public class UserDTO {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Integer id;

    //    @Column
    @Schema(description = "Имя пользователя")
    @NotBlank
    private String name;

    //    @Column
    @Schema(description = "Электронная почта", example = "junior@example.com")
    @Email
    @NotBlank
    private String email;

    //    @Column
    @Schema(description = "Пароль должен содержать от 8 до 32 символов," +
            "как минимум одну букву, одну цифру и один специальный символ")
    @Size(min = 8, max = 32)
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?^&])[A-Za-z\\d@$!%*#?^&]{3,}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Schema(description = "Роль пользователя")
    private String role;
}
