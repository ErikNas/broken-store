package ru.eriknas.brokenstore.components.profanityValidator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfanityValidateRequest {
    private String phrase;
}
