package ru.eriknas.brokenstore.exception;

public class InternalException extends RuntimeException {

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}