package ru.eriknas.brokenstore.mappers;

import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.models.entities.UsersEntity;

public class UsersMapper {

    public static UserDTO toDto(UsersEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .archivedAt(entity.getArchivedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UsersEntity toEntity(UserDTO dto) {
        return UsersEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
