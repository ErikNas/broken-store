package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.mappers.UsersMapper;
import ru.eriknas.brokenstore.models.entities.UsersEntity;
import ru.eriknas.brokenstore.services.UsersService;

import java.util.Collection;
import java.util.Optional;
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
    @SecurityRequirements
    @Operation(summary = "Добавить пользователя")
    public UserDTO addUser(@RequestBody @Validated UserDTO userDTO) {
        UsersEntity usersEntity = usersService.addUsers(userDTO);
        return UsersMapper.toDto(usersEntity);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirements
    @Operation(summary = "Удалить пользователя")
    public void deleteUsers(@PathVariable @Validated @Parameter(description = "id пользователя") int id) {
        usersService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @Operation(summary = "Найти пользователя по id")
    public Optional<UserDTO> getUsersById(@PathVariable @Validated @Parameter(description = "id пользователя") int id) {
        Optional<UsersEntity> users = usersService.getUsersById(id);
        return users.map(UsersMapper::toDto);
    }

    @GetMapping
    @Operation(summary = "Получить список всех сотрудников")
    public Collection<UserDTO> getAllUsers(@RequestParam(required = false, defaultValue = "0")
                                           @Parameter(description = "min: 0") //раз
                                           @Validated @Min(0) int page,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Parameter(description = "min: 1") //двас
                                           @Validated @Min(1) int size) {
//        return List.of(new UserDTO("retrieved user", "junior@example.com", null));
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
