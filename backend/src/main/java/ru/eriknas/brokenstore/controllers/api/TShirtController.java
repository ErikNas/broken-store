package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.store.TShirtsDTO;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.services.TShirtService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/t-shirt")
@Validated
@Tag(name = "Футболки")
public class TShirtController {

    private final TShirtService tShirtsService;

    @Autowired
    public TShirtController(TShirtService tShirtsService) {
        this.tShirtsService = tShirtsService;
    }

    @PostMapping
    @Operation(summary = "Добавить футболку")
    @ApiResponse(responseCode = "201 Created", description = "Создана новая футболка")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @SecurityRequirements
    public ResponseEntity<?> createTShirt(@RequestBody @Validated TShirtsDTO dto) {
        TShirtsEntity created = tShirtsService.createTShirt(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить футболку")
    @ApiResponse(responseCode = "200 OK", description = "Изменения внесены")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<TShirtsEntity> updateTShirt(@PathVariable
                                                      @Validated
                                                      @Parameter(description = "id футболки") int id,
                                                      @RequestBody @Valid TShirtsDTO dto) {
        TShirtsEntity updated = tShirtsService.updateTShirt(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить футболку")
    @ApiResponse(responseCode = "204 NoContent", description = "Футболка удалена")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Void> deleteTShirt(@PathVariable
                                             @Validated
                                             @Parameter(description = "id футболки") int id) {
        tShirtsService.getTShirtById(id);
        tShirtsService.deleteTShirt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти футболку по id")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<TShirtsEntity> getTShirtById(@PathVariable
                                                       @Validated
                                                       @Parameter(description = "id футболки") int id) {
        TShirtsEntity dto = tShirtsService.getTShirtById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех футболок")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Collection<TShirtsDTO>> getAllTShirts(@RequestParam(required = false, defaultValue = "0")
                                                @Parameter(description = "min: 0")
                                                @Validated @Min(0) int page,
                                                @RequestParam(required = false, defaultValue = "10")
                                                @Parameter(description = "min: 1")
                                                @Validated @Min(1) int size) {
        if (page < 0 || size < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Collection<TShirtsDTO> tShirts = tShirtsService.getAllTShirts(page, size)
                .get()
                .map(TShirtsMapper::toDto)
                .collect(Collectors.toList());

        if (tShirts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tShirts);
    }
}