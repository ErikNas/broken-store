package ru.eriknas.brokenstore.mappers;

import ru.eriknas.brokenstore.dto.users.UserDTO;
import ru.eriknas.brokenstore.models.entities.UsersEntity;

public class UsersMapper {

    public static UserDTO toDto(UsersEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

    public static UsersEntity toEntity(UserDTO dto) {
        return UsersEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
