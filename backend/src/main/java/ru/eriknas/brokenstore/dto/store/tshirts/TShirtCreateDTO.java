package ru.eriknas.brokenstore.dto.store.tshirts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Добавление футболки")
public class TShirtCreateDTO {

    @Schema(hidden = true)
    private Integer id;

    @Schema(description = "Артикул футболки")
    @NotNull(message = "Укажите артикул футболки")
    private String article;

    @Schema(description = "Название футболки")
    @NotNull(message = "Укажите название футболки")
    private String name;

    @Schema(description = "Размер футболки")
    @NotNull(message = "Укажите размер футболки")
    private String size;

    @Schema(description = "Цвет футболки")
    @NotNull(message = "Укажите цвет футболки")
    private String color;

    @Schema(description = "Изображение на футболке")
    private String image;

    @Schema(description = "Материал футболки")
    @NotNull(message = "Укажите материал футболки")
    private String material;

    @Schema(description = "Страна производства")
    @NotNull(message = "Укажите страну производства")
    private String countryOfProduction;

    @Schema(description = "Дополнительное описание, в т.ч. теги")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#.##")
    @Schema(description = "Цена футболки")
    @NotNull(message = "Укажите цену футболки")
    @Min(value = 0, message = "Цена не может быть отрицательная")
    private Double price;
}