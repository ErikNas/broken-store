package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.exception.EmailAlreadyExistsException;
import ru.eriknas.brokenstore.mappers.UsersMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.models.entities.UsersEntity;
import ru.eriknas.brokenstore.services.UsersService;
import ru.eriknas.brokenstore.services.keycloak.KeycloakUserService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UserController {

    private final UsersService usersService;
    private final KeycloakUserService keycloakUserService;

    @Autowired
    public UserController(UsersService usersService, KeycloakUserService keycloakUserService) {
        this.usersService = usersService;
        this.keycloakUserService = keycloakUserService;
    }

    @PostMapping
    @Operation(summary = "Добавить пользователя")
    @ApiResponse(responseCode = "201 Created", description = "Пользователь добавлен")
    @ApiResponse(responseCode = "400  Bad Request", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "422", description = "Email уже существует",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createUser(@RequestBody @Validated UserDTO dto) throws Exception {
        if (!isValidPassword(dto.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error("Пароль не соответствует требованиям или содержит недопустимые символы."));
        }

        UsersEntity usersEntity = usersService.addUsers(dto);
        keycloakUserService.addUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204 NoContent", description = "Пользователь удален")
    @ApiResponse(responseCode = "404 NotFound", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUsers(@PathVariable
                                            @Validated
                                            @Parameter(description = "id пользователя") int id) {
        usersService.getUsersById(id);
        usersService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти пользователя по id")
//    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
//    todo вернуть роли. Отключено для работы backend-service
//     @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//    public ResponseEntity<UsersEntity> getUsersById(@PathVariable
    public ResponseEntity<UserDTO> getUsersById(@PathVariable
                                                    @Validated
                                                    @Parameter(description = "id пользователя") int id) {
//        UsersEntity dto = usersService.getUsersById(id);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
        UsersEntity usersEntity = usersService.getUsersById(id);
        UserDTO userDTO = UsersMapper.toDto(usersEntity);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех сотрудников")
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
//    @ApiResponse(responseCode = "200 OK")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Collection<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0")
                                           @Parameter(description = "min: 0")
                                           @Validated @Min(0) int page,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Parameter(description = "min: 1")
                                           @Validated @Min(1) int size) {
        return usersService.getAllUsers(page, size)
                .get()
                .map(UsersMapper::toDto)
                .collect(Collectors.toList());
    }

    //    @GetMapping("/{email}")
//    // тело ответа добавится в конфигурации
//    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
//    public UserDTO getUserByEmail(@PathVariable @Validated @Email
//                                  @Parameter(description = "Электронная почта", example = "junior@example.com")
//                                  String email) {
////        return new UserRepositories().findBy()
////        UserRepositories userRepositories = new UserRepositories();
////        return .findById("1");
//        return new UserDTO("retrieved user", email, null);
//    }

    @GetMapping("/page")
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    public Page<UserDTO> getAllUsersAsPage(Pageable pageable) {
        return Page.empty(pageable);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Error> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        Error error = new Error(ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(error);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && !password.contains("\\");
    }
}