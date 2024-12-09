package ru.eriknas.brokenstore.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
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
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
    @SecurityRequirements
    public ResponseEntity<NewsDTO> addNews(@RequestBody @Validated NewsDTO newsDTO) {
        NewsEntity newsEntity = newsService.addNews(newsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(NewsMapper.toDto(newsEntity));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Новость удалена")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @SecurityRequirements
    public ResponseEntity<Void> deleteNews(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        if (newsService.getNewsById(id).isPresent()) {
            newsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable @Validated @Parameter(description = "id новости") int id) {
        return newsService.getNewsById(id)
                .map(NewsMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @ApiResponse(responseCode = "200", useReturnTypeSchema = true)
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