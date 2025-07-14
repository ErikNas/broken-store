package ru.eriknas.brokenstore.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

public class GlobalValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }
    //глобальная проверка контроллеров
//    @ControllerAdvice
//    public static class GlobalExceptionHandler {
//        @ExceptionHandler({EntityNotFoundException.class, IllegalArgumentException.class})
//        public ResponseEntity<Error> handleNotFound() {
//            return ResponseEntity.notFound().build(); // 404
//        }
//    }

    /**
     * Обрабатывает отсутствующие обязательные параметры запроса
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMissingParams(
            MissingServletRequestParameterException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", "Обязательный параметр отсутствует: " + ex.getParameterName());

        return ResponseEntity.badRequest().body(error);
    }
//создал

    //обработка пустого ID (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String>handleIllegalArgument(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    //Обработка отсутствия сущности (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String>handleEntityNotFound(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    //обработка некорректного URL(404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String>handleNoHandlerFound(NoHandlerFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Некорректный URL" + ex.getRequestURL());
    }

    /**
     * Обрабатывает некорректные типы данных в запросе
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, String> error = new HashMap<>();
        error.put("message", "Некорректный тип данных: " + ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Обрабатывает нечитаемые тела запросов (невалидный JSON и т.д.)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleInvalidRequestBody(
            HttpMessageNotReadableException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", "Некорректное тело запроса: " + ex.getMostSpecificCause().getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
