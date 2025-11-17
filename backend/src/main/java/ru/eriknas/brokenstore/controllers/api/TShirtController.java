package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtCreateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtUpdateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtsInfoDTO;
import ru.eriknas.brokenstore.mappers.TShirtsMapper;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;
import ru.eriknas.brokenstore.services.MinioService;
import ru.eriknas.brokenstore.services.TShirtService;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    public ResponseEntity<?> createTShirt(@RequestPart @Validated TShirtCreateDTO dto,
                                          @RequestPart(value = "picture", required = false) MultipartFile picture) {
        TShirtsInfoDTO created = tShirtsService.createTShirt(dto);

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
    public ResponseEntity<TShirtsInfoDTO> updateTShirt(@PathVariable
                                                      @Validated
                                                      @Parameter(description = "id футболки") String id,
                                                      @RequestBody @Valid TShirtUpdateDTO dto) {
        TShirtsEntity updated = tShirtsService.updateTShirt(id, dto);
        return new ResponseEntity<>(TShirtsMapper.toDto(updated), HttpStatus.OK);
//        return new ResponseEntity<>(updated, HttpStatus.OK);
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
    @ApiResponse(responseCode = "404 NotFound", description = "Футболка не найдена",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<TShirtsEntity> getTShirtById(@PathVariable
                                                       @Validated
                                                       @Parameter(description = "id футболки") String id) {
        TShirtsEntity dto = tShirtsService.getTShirtById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
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
                                                                    @RequestParam(required = false)
                                                                    @Parameter(description = "Фильтр по статусу активности: true - активные, false - неактивные, если параметр не указан или пуст - все")
                                                                    String isActiveFilter) {

        System.out.println("DEBUG: Controller received - page: " + page + ", size: " + size + ", isActiveFilter: '" + isActiveFilter + "'"); // DEBUG

        if (page < 0 || size < 1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


//        Collection<TShirtsInfoDTO> tShirts = tShirtsService.getAllTShirts(page, size, isActive)
//                .getContent()
//                .stream()
//                .map(TShirtsMapper::toDto)
//                .collect(Collectors.toList());

        Collection<TShirtsInfoDTO> tShirts;
        Page<TShirtsEntity> pageResult;

        // Проверяем, был ли параметр isActiveFilter передан и не пустой ли он
        if (isActiveFilter == null || isActiveFilter.trim().isEmpty()) {
            System.out.println("DEBUG: Controller - isActiveFilter is null or empty, fetching ALL t-shirts."); // DEBUG
            // Если фильтр не выбран (не передан или пуст), получаем все записи (и isActive=true, и isActive=false)
            pageResult = tShirtsService.getAllTShirts(page, size); // Вызываем метод без фильтра
        } else {
            // Пытаемся распарсить строку в boolean
            boolean parsedIsActive;
            try {
                // parseBoolean сам обработает "true"/"false" независимо от регистра
                parsedIsActive = Boolean.parseBoolean(isActiveFilter.toLowerCase());
                // Проверим, действительно ли строка была "true" или "false", а не что-то другое
                if (!"true".equalsIgnoreCase(isActiveFilter) && !"false".equalsIgnoreCase(isActiveFilter)) {
                    System.out.println("DEBUG: Controller - Invalid value for isActiveFilter: " + isActiveFilter); // DEBUG
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            } catch (Exception e) { // Ловим любое исключение при парсинге
                System.out.println("DEBUG: Controller - Exception parsing isActiveFilter: " + e.getMessage()); // DEBUG
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            System.out.println("DEBUG: Controller - isActiveFilter is " + parsedIsActive + ", fetching filtered t-shirts."); // DEBUG
            // Если фильтр выбран (true или false), получаем только соответствующие
            pageResult = tShirtsService.getAllTShirts(page, size, parsedIsActive); // Вызываем метод с фильтром
        }

        // Проверяем результат из сервиса
        System.out.println("DEBUG: Controller - Page result size: " + pageResult.getContent().size() + ", Total elements: " + pageResult.getTotalElements()); // DEBUG

        tShirts = pageResult.getContent()
                .stream()
                .map(TShirtsMapper::toDto) // <-- Ошибка может быть здесь, если что-то не так с сущностью или маппером
                .collect(Collectors.toList());


        if (tShirts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tShirts);
    }
}