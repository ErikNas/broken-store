package ru.eriknas.brokenstore.dto.store.tshirts;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Информация о футболке")
public class TShirtsInfoDTO {

    @Schema(description = "Идентификатор футболки")
    private Integer id;

    @Schema(description = "Артикул футболки")
    private String article;

    @Schema(description = "Название футболки")
    private String name;

    @Schema(description = "Размер футболки")
    private String size;

    @Schema(description = "Цвет футболки")
    private String color;

    @Schema(description = "Изображение на футболке")
    private String image;

    @Schema(description = "Материал футболки")
    private String material;

    @Schema(description = "Страна производства")
    private String countryOfProduction;

    @Schema(description = "Дополнительное описание, в т.ч. теги")
    private String description;

    @Schema(description = "Цена футболки")
    private Double price;

    @JsonProperty("isActive")
    @Schema(description = "Флаг активности футболки")
    private boolean isActive;

    @Schema(description = "Дата/время удаления записи")
    private LocalDate archivedAt;

    @Schema(description = "Дата/время создания записи")
    private LocalDate createdAt;

    @Schema(description = "Дата/время обновления записи")
    private LocalDate updatedAt;
}