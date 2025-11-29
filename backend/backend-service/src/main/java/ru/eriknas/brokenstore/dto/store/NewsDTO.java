package ru.eriknas.brokenstore.dto.store;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Новости")
public class NewsDTO {

    @Schema(hidden = true)
    private int id;

    @Schema(description = "Название новости")
    @NotNull(message = "Укажите название новости")
    private String header;

    @Schema(description = "Содержимое статьи")
    @NotNull(message = "Укажите содержимое статьи")
    private String description;
}