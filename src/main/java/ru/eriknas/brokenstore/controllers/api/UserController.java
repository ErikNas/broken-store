package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.entities.UserDTO;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
//дефолтный ответ для всех запросов
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class UserController {

    @PostMapping
    @SecurityRequirements
    public UserDTO addUser(@RequestBody @Validated UserDTO userDTO) {
        return userDTO;
    }

    @GetMapping("/{email}")
    // тело ответа добавится в конфигурации
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public UserDTO getUserByEmail(@PathVariable @Validated @Email
                                  @Parameter(description = "Электронная почта", example = "junior@example.com")
                                  String email) {
//        return new UserRepositories().findBy()
//        UserRepositories userRepositories = new UserRepositories();
//        return .findById("1");
        return new UserDTO("retrieved user", email, null);
    }

    @GetMapping
    public Collection<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0")
                                           @Parameter(description = "min: 0") //раз
                                           @Validated @Min(0) int page,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Parameter(description = "min: 1") //двас
                                           @Validated @Min(1) int size) {
        return List.of(new UserDTO("retrieved user", "junior@example.com", null));
    }

    @GetMapping("/page")
    public Page<UserDTO> getAllUsersAsPage(Pageable pageable) {
        return Page.empty(pageable);
    }
}
