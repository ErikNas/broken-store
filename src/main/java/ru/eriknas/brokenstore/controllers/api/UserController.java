package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.exception.UserNotFoundException;
import ru.eriknas.brokenstore.model.User;
import ru.eriknas.brokenstore.services.UserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
//дефолтный ответ для всех запросов
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class UserController {

    private Map<String, User> userDatabase = new HashMap<>();
    private Map<String, Boolean> userSessions = new HashMap<>();

    @Autowired
    private UserService userService;

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

    @PostMapping("/user")
    @Operation(summary = "Создать пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить пользователя по username")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Пользователь с username: " + username + " не найден");
        }
    }

    @Operation(summary = "Обновить данные о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о пользователе обновлены",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @PutMapping("/user/{username}")
    public  ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        if (!userDatabase.containsKey(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user == null || username == null || username.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userDatabase.put(username, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/user/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        if (!userDatabase.containsKey(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDatabase.remove(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Залогинить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь залогинен"),
            @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль")
    })
    @GetMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        if (!userDatabase.containsKey(username) || !userDatabase.get(username).equals(password)) {
            return new ResponseEntity<>("Неверное имя пользователя или пароль", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Пользователь залогинен", HttpStatus.OK);
    }


    @Operation(summary = "Логаут пользователя из сессии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно логаутен"),
            @ApiResponse(responseCode = "400", description = "Ошибка при логауте пользователя")
    })
    @GetMapping("/user/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String username) {
        // Логика для логаута пользователя из сессии
        boolean isLoggedOut = logoutUserFromSession(username);
        if (!isLoggedOut) {
            return new ResponseEntity<>("Ошибка при логауте пользователя", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Пользователь успешно логаутен", HttpStatus.OK);
    }

    public boolean logoutUserFromSession(String username) {
        if (userSessions.containsKey(username) && userSessions.get(username)) {
            userSessions.put(username, false);
            return true;
        }
        return false;
    }
}
