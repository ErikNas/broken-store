package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.eriknas.brokenstore.dto.store.NewsDTO;
import ru.eriknas.brokenstore.mappers.NewsMapper;
import ru.eriknas.brokenstore.models.entities.NewsEntity;
import ru.eriknas.brokenstore.services.NewsService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Добавить новость")
    public NewsDTO addNews(@RequestBody @Validated NewsDTO newsDTO) {
        NewsEntity newsEntity = newsService.addNews(newsDTO);
        return NewsMapper.toDto(newsEntity);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirements
    @Operation(summary = "Удалить новость")
    public void deleteNews(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        newsService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @Operation(summary = "Найти новость по id")
    public Optional<NewsDTO> getNewsById(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        Optional<NewsEntity> news = newsService.getNewsById(id);
        return news.map(NewsMapper::toDto);
    }

    @GetMapping
    @Operation(summary = "Получить списсок всех новостей")
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
