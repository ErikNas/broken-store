package ru.eriknas.brokenstore.controllers.api;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.eriknas.brokenstore.exception.*;
import ru.eriknas.brokenstore.model.Error;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.NOT_FOUND)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        var message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" ,"));

        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(message)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalException.class)
    protected ResponseEntity<Object> handleInternalException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.UNEXPECTED)
                .message(ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.UNEXPECTED)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Error> handleDataIntegrityViolation() {
        String errorMessage = "Значение должно быть уникальным.";

        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIdException.class)
    protected ResponseEntity<Error> handleInvalidIdException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.INVALID_CREDENTIALS)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPageSizeException.class)
    protected ResponseEntity<Error> handleInvalidPageSizeException(Exception ex) {

        var error = Error.builder()
                .type(Error.Type.INVALID_CREDENTIALS)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message("Неправильный формат данных: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}