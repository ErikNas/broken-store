package ru.eriknas.brokenstore.components.profanityValidator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfanityValidateResponse {
    private int predict;

    @JsonProperty("predict_proba")
    private double predictProba;
}
