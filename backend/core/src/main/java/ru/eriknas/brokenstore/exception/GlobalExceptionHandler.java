package ru.eriknas.brokenstore.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.eriknas.brokenstore.models.entities.Error;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfanityException.class)
    public ResponseEntity<Object> handleProfanityException(ProfanityException e) {
        var error = Error.builder()
                .type(Error.Type.VALIDATION_ERROR)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> handleJsonMappingException(JsonMappingException ex) {
        return ResponseEntity.badRequest().body("Invalid request payload: " + ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        var error = Error.builder()
                .type(Error.Type.FORBIDDEN_ERROR)
                .message("Access denied")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
