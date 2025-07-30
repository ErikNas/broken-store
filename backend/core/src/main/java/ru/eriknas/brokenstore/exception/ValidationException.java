package ru.eriknas.brokenstore.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}