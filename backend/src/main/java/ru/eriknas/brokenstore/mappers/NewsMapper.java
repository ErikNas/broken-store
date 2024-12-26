package ru.eriknas.brokenstore.mappers;

import ru.eriknas.brokenstore.dto.store.NewsDTO;
import ru.eriknas.brokenstore.entity.NewsEntity;

public class NewsMapper {

    public static NewsDTO toDto(NewsEntity entity) {
        return NewsDTO.builder()
                .id(entity.getId())
                .header(entity.getHeader())
                .description(entity.getDescription())
                .build();
    }

    public static NewsEntity toEntity(NewsDTO dto) {
        return NewsEntity.builder()
                .header(dto.getHeader())
                .description(dto.getDescription())
                .build();
    }
}
