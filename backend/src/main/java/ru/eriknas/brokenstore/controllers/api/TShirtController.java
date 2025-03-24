package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.eriknas.brokenstore.dto.store.TShirtsDTO;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.services.MinioService;
import ru.eriknas.brokenstore.services.TShirtService;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/t-shirt")
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
    public ResponseEntity<?> createTShirt(@RequestPart @Validated TShirtsDTO dto,
                                          @RequestPart(value = "picture", required = false) MultipartFile picture) {
        TShirtsEntity created = tShirtsService.createTShirt(dto);

        String fileNameInMinio;
        try {
            fileNameInMinio = minioService.uploadFileToMinIO(picture, dto.getArticle());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Error("Произошла ошибка загрузки файла: " + e.getMessage()));
        }
        created.setImage(fileNameInMinio);
        return new ResponseEntity<>(TShirtsMapper.toDto(created), HttpStatus.CREATED);
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
                                                      @RequestBody TShirtsDTO dto) {
        TShirtsEntity updated = tShirtsService.updateTShirt(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить футболку")
    @ApiResponse(responseCode = "204 NoContent", description = "Футболка удалена")
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ResponseEntity<Void> deleteTShirt(@PathVariable
                                             @Validated
                                             @Parameter(description = "id футболки") int id) throws Exception {
        TShirtsEntity tShirt = tShirtsService.getTShirtById(id);
        tShirtsService.deleteTShirt(id);
        minioService.removeFile(tShirt.getImage());
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
    public Collection<TShirtsDTO> getAllTShirts(@RequestParam(required = false, defaultValue = "0")
                                                @Parameter(description = "min: 0")
                                                @Validated @Min(0) int page,
                                                @RequestParam(required = false, defaultValue = "10")
                                                @Parameter(description = "min: 1")
                                                @Validated @Min(1) int size) {
        return tShirtsService.getAllTShirts(page, size)
                .get()
                .map(TShirtsMapper::toDto)
                .collect(Collectors.toList());
    }
}