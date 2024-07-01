package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.model.TShirt;
import ru.eriknas.brokenstore.services.TShirtService;
import ru.eriknas.brokenstore.services.TShirtRepository;

import java.util.List;

@RestController
public class TShirtController {
    private final TShirtRepository tShirtRepository;
    @Autowired
    private TShirtService tShirtService;

    @Autowired
    public TShirtController(TShirtRepository tShirtRepository) {
        this.tShirtRepository = tShirtRepository;
    }

    @PostMapping("/t-shirt")
    @Operation(summary = "Добавить новую футболку в магазин")
    @ApiResponse(responseCode = "200", description = "Новая футболка успешно добавлена")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    public ResponseEntity<String> addTShirt(@RequestBody TShirt newTShirt) {
        // Проверка данных о футболке
        if (newTShirt.getBrand() == null || newTShirt.getSize() == null || newTShirt.getColor() == null
                || newTShirt.getGender() == null || newTShirt.getPrice() <= 0 || newTShirt.getId()!= null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректные данные о футболке");
        }
        // Сохранение футболки в базу данных
        TShirt savedTShirt = tShirtRepository.save(newTShirt);
        // Ответ клиенту
        return ResponseEntity.ok("Футболка с ID " + savedTShirt.getId() + " добавлена");
    }

    @PutMapping("/t-shirt")
    @Operation(summary = "Обновить информацию по существующей футболке")
    @ApiResponse(responseCode = "200", description = "Футболка успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Футболка не найдена")
    public TShirt updateTShirt(@RequestBody TShirt tShirt) {
        return tShirtService.updateTShirt(tShirt);
    }

    @Operation(summary = "Обновить информацию о футболке. Передаются только конкретные поля в параметрах.")
    @ApiResponse(responseCode = "200", description = "Информация о футболке успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Футболка не найдена")
    @PostMapping("/t-shirt/{tShirtId}")
    public TShirt updateTShirt(@PathVariable Long tShirtId, @RequestBody TShirt updateRequest) {
        return tShirtService.updateTShirt(tShirtId, updateRequest);
    }

    @GetMapping("/t-shirt/findByColor")
    @Operation(summary = "Найти футболку нужного цвета")
    @ApiResponse(responseCode = "200", description = "Список футболок нужного цвета найден")
    @ApiResponse(responseCode = "404", description = "Футболки данного цвета не найдены")
    public List<TShirt> findTShirtsByColor(@RequestParam String color) {
        return tShirtService.findTShirtsByColor(color);
    }

    @Operation(summary = "Найти футболки по тэгам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Футболки с заданными тэгами найдены"),
            @ApiResponse(responseCode = "404", description = "Футболки с такими тэгами не найдены")
    })
    @GetMapping("/t-shirt/findByTags")
    public List<TShirt> findTShirtsByTags(@RequestParam List<String> tags) {
        return tShirtService.findTShirtsByTags(tags);
    }

    @Operation(summary = "Получить футболку по артикулу")
    @ApiResponse(responseCode = "200", description = "Футболка успешно найдена")
    @ApiResponse(responseCode = "404", description = "Футболка не найдена")
    @GetMapping("/t-shirt/{tShirtId}")
    public TShirt getTShirtById(@PathVariable Long tShirtId) {
        return tShirtService.findById(tShirtId);
    }

    @Operation(summary = "Удалить футболку из каталога")
    @ApiResponse(responseCode = "200", description = "Футболка успешно удалена")
    @ApiResponse(responseCode = "404", description = "Футболка не найдена")
    @DeleteMapping("/t-shirt/{tShirtId}")
    public ResponseEntity<?> deleteTShirt(@PathVariable Long tshirtId) {
        tShirtService.deleteTShirt(tshirtId);
        return ResponseEntity.ok().build();
    }
}
