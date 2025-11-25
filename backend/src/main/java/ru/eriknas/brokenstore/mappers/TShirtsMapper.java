package ru.eriknas.brokenstore.mappers;

import org.jetbrains.annotations.Contract;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtCreateDTO;
import ru.eriknas.brokenstore.dto.store.tshirts.TShirtsInfoDTO;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TShirtsMapper {

    public static TShirtsInfoDTO toDto(TShirtsEntity entity) {

        return TShirtsInfoDTO.builder()
                .id(entity.getId())
                .article(entity.getArticle())
                .name(entity.getName())
                .size(entity.getSize())
                .color(entity.getColor())
                .image(entity.getImage())
                .material(entity.getMaterial())
                .countryOfProduction(entity.getCountryOfProduction())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .isActive(entity.getIsActive() != null ? entity.getIsActive() : true)
                .build();

    }

    public static TShirtsEntity toEntity(TShirtCreateDTO dto) {
        return TShirtsEntity.builder()
                .article(dto.getArticle())
                .name(dto.getName())
                .size(dto.getSize())
                .color(dto.getColor())
                .image(dto.getImage())
                .material(dto.getMaterial())
                .countryOfProduction(dto.getCountryOfProduction())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .isActive(dto.getIsActive())
                .build();
    }
}