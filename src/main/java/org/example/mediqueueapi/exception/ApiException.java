package org.example.mediqueueapi.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
