package ru.eriknas.brokenstore.dto.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.eriknas.brokenstore.controllers.api.StrictDoubleDeserializer;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Футболки")
public class TShirtsDTO {

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

    @Schema(description = "Цена футболки")
    @NotNull(message = "Укажите цену футболки")
    @Min(value = 0, message = "Цена не может быть отрицательная")
    @JsonDeserialize(using = StrictDoubleDeserializer.class)
    private Double price;

    @JsonCreator
    public static TShirtsDTO fromJson(
            @JsonProperty("id") Integer id,
            @JsonProperty("article") String article,
            @JsonProperty("name") String name,
            @JsonProperty("size") String size,
            @JsonProperty("color") String color,
            @JsonProperty("image") String image,
            @JsonProperty("material") String material,
            @JsonProperty("countryOfProduction") String countryOfProduction,
            @JsonProperty("description") String description,
            @JsonProperty("price") Double price) {
        if (price == null || price != price.doubleValue()) {
            throw new IllegalArgumentException("Не верный формат поля");
        }
        return new TShirtsDTO(id, article, name, size, color, image, material, countryOfProduction, description, price);
    }
}