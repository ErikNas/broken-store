package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtCreateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtUpdateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtsInfoDTO;

import org.springframework.web.multipart.MultipartFile;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.services.MinioService;
import ru.eriknas.brokenstore.services.TShirtService;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/t-shirt")
@Validated
@Tag(name = "Футболки")
public class TShirtController {

    private final TShirtService tShirtsService;
    private final MinioService minioService;

    @Autowired
    public TShirtController(TShirtService tShirtsService, MinioService minioService) {
        this.tShirtsService = tShirtsService;
        this.minioService = minioService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавить футболку")
    @ApiResponse(responseCode = "201 Created", description = "Создана новая футболка")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseEntity<?> createTShirt(@RequestBody @Validated TShirtCreateDTO dto,
                                          @RequestPart(value = "picture", required = false) MultipartFile picture) {
        TShirtsInfoDTO created = tShirtsService.createTShirt(dto);
        dto.setActive(true); //Автоматическое создание статуса isActive = true

        String fileNameInMinio;
        try {
            fileNameInMinio = minioService.uploadFileToMinIO(picture, dto.getArticle());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Error("Произошла ошибка загрузки файла: " + e.getMessage()));
        }
        created.setImage(fileNameInMinio);
//        return new ResponseEntity<>(TShirtsMapper.toDto(created), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать футболку")
    @ApiResponse(responseCode = "200 OK", description = "Изменения внесены")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<TShirtsEntity> updateTShirt(@PathVariable
                                                      @Validated
                                                      @Parameter(description = "id футболки") String id,
                                                      @RequestBody @Valid TShirtUpdateDTO dto) {
        TShirtsEntity updated = tShirtsService.updateTShirt(id, dto);
        return ResponseEntity.ok(updated); //изменение статуса(поле isActive автоматом обрабатывается через ДТО)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить футболку")
    @ApiResponse(responseCode = "204 NoContent", description = "Футболка удалена")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseEntity<Void> deleteTShirt(@PathVariable
                                             @Validated
                                             @Parameter(description = "id футболки") String id) throws Exception {
        TShirtsEntity tShirt = tShirtsService.getTShirtById(id);
        tShirtsService.deleteTShirt(id);
        minioService.removeFile(tShirt.getImage());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти футболку по id")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "400 BadRequest", description = "ID футболки не может быть пустым")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))


    public ResponseEntity<?> getTShirtById(@PathVariable
                                           @Validated
                                           @NotBlank
                                           @Parameter(description = "id футболки") String id) {
        //добавлена проверка на пустой ввод ID
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("ID футболки не может быть пустым"); //404
        }
        try {
            TShirtsEntity dto = tShirtsService.getTShirtById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех футболок")
    @ApiResponse(responseCode = "200 OK")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<Collection<TShirtsInfoDTO>> getAllTShirts(@RequestParam(required = false, defaultValue = "0")
                                                                    @Parameter(description = "min: 0")
                                                                    @Validated @Min(0) int page,
                                                                    @RequestParam(required = false, defaultValue = "10")
                                                                     @Parameter(description = "min: 1")
                                                                    @Validated @Min(1) int size,
                                                                    @RequestParam(required = false) Boolean isActive) {
        if (page < 0 || size < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Collection<TShirtsInfoDTO> tShirts = tShirtsService.getAllTShirts(page, size)
                .get()
                .map(TShirtsMapper::toDto)
                .collect(Collectors.toList());
        return !tShirts.isEmpty() ? ResponseEntity.ok(tShirts) : ResponseEntity.notFound().build();

//Излишняя валидация???
//        if (tShirts.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.ok(tShirts);
    }
    //todo Написать реализацию метода внизу, и добавить его в методы выше, добавить в create t-Shirt DTO
}

