package ru.eriknas.brokenstore.mappers;

import ru.eriknas.brokenstore.dto.store.TShirtsDTO;
import ru.eriknas.brokenstore.models.entities.TShirtsEntity;

public class TShirtsMapper {

    public static TShirtsDTO toDto(TShirtsEntity entity) {
        return TShirtsDTO.builder()
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
                .archivedAt(entity.getArchivedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TShirtsEntity toEntity(TShirtsDTO dto) {
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
                .build();
    }
}