package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.store.NewsDTO;
import ru.eriknas.brokenstore.mappers.NewsMapper;
import ru.eriknas.brokenstore.models.entities.Error;
import ru.eriknas.brokenstore.models.entities.NewsEntity;
import ru.eriknas.brokenstore.services.NewsService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    @ApiResponse(
            responseCode = "400 BadRequest",
            description = "Ошибка валидации",
            content = @Content(schema = @Schema(implementation = Error.class))
    )
    @SecurityRequirements
    public ResponseEntity<NewsDTO> addNews(@RequestBody @Validated NewsDTO newsDTO) {
        NewsEntity newsEntity = newsService.addNews(newsDTO);
        return new ResponseEntity<>(NewsMapper.toDto(newsEntity), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204 NoContent", description = "Новость удалена")
    @ApiResponse(responseCode = "404 NotFound", description = "Новость не найдена")
    @SecurityRequirements
    public ResponseEntity<Void> deleteNews(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        newsService.getNewsById(id);
        newsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404 NotFound", description = "Новость не найдена", content = @Content)
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        NewsEntity newsEntity = newsService.getNewsById(id);
        NewsDTO newsDTO = NewsMapper.toDto(newsEntity);
        return new ResponseEntity<>(newsDTO, HttpStatus.OK);

    }

    @GetMapping
    @ApiResponse(responseCode = "200 OK", useReturnTypeSchema = true)
    public Collection<NewsDTO> getAllNews(@RequestParam(required = false, defaultValue = "0")
                                          @Parameter(description = "min: 0")
                                          @Validated @Min(0) int page,
                                          @RequestParam(required = false, defaultValue = "10")
                                          @Parameter(description = "min: 1")
                                          @Validated @Min(1) int size) {

        return newsService.getAllNews(page, size)
                .get()
                .map(NewsMapper::toDto)
                .collect(Collectors.toList());
    }
}