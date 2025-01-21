package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.mappers.UsersMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.models.entities.UsersEntity;
import ru.eriknas.brokenstore.services.UsersService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    @Operation(summary = "Добавить пользователя")
    @ApiResponse(responseCode = "201 Created", description = "Пользователь добавлен")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @SecurityRequirements
    public ResponseEntity<UserDTO> addUser(@RequestBody @Validated UserDTO dto) {
        UsersEntity usersEntity = usersService.addUsers(dto);
        return new ResponseEntity<>(UsersMapper.toDto(usersEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirements
    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204 NoContent", description = "Пользователь удален")
    @ApiResponse(responseCode = "404 NotFound", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Void> deleteUsers(@PathVariable
                                @Validated
                                @Parameter(description = "id пользователя") int id) {
        usersService.getUsersById(id);
        usersService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти пользователя по id")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
    content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<UsersEntity> getUsersById(@PathVariable
                                                        @Validated
                                                        @Parameter(description = "id пользователя") int id) {
        UsersEntity dto = usersService.getUsersById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех сотрудников")
    @ApiResponse(responseCode = "200 OK")
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
    public Page<UserDTO> getAllUsersAsPage(Pageable pageable) {
        return Page.empty(pageable);
    }
}
