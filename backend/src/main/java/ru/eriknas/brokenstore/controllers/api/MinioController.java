package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.services.MinioService;

@RestController
@Tag(name = "Minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    public MinioController() {
    }

    @GetMapping("/download")
    @Operation(summary = "Получить файл из minio")
    @ApiResponse(responseCode = "200 OK", description = "Файл скачан")
    @ApiResponse(responseCode = "400 BadRequest", description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class)))
    @ApiResponse(responseCode = "404 NotFound", description = "Файл не найден",
            content = @Content(schema = @Schema(implementation = Error.class)))
    public ResponseEntity<?> downloadFile(@RequestParam("file_name") @NotNull String fileName) throws Exception {

            String file = minioService.downloadFile(fileName);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(file);
    }
}