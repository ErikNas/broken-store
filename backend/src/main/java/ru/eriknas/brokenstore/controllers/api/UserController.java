package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
//дефолтный ответ для всех запросов
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class UserController {

    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @SecurityRequirements
    @Operation(summary = "Добавить пользователя")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Validated UserDTO userDTO) {
        UsersEntity usersEntity = usersService.addUsers(userDTO);
        return new ResponseEntity<>(UsersMapper.toDto(usersEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204 NoContent", description = "Новость удалена")
    @ApiResponse(responseCode = "404 NotFound", description = "Новость не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @SecurityRequirements
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> deleteUsers(@PathVariable
                                            @Validated
                                            @Parameter(description = "id пользователя") int id) {
        usersService.getUsersById(id);
        usersService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    @Operation(summary = "Найти пользователя по id")
    public ResponseEntity<UserDTO> getUsersById(@PathVariable
                                                @Validated
                                                @Parameter(description = "id пользователя") int id) {
        UsersEntity usersEntity = usersService.getUsersById(id);
        UserDTO userDTO = UsersMapper.toDto(usersEntity);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Получить список всех сотрудников")
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    public Collection<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0")
                                           @Parameter(description = "min: 0") //раз
                                           @Validated @Min(0) int page,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Parameter(description = "min: 1") //двас
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
}
