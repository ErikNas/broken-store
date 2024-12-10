package ru.eriknas.brokenstore.models.entities;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String message;
    private Type type;

    public enum Type {
        UNEXPECTED,
        VALIDATION_ERROR,
        NOT_FOUND,
        FILE_STORAGE_ERROR,
        DOCUMENT_ERROR,
        FORBIDDEN_ERROR
    }
}