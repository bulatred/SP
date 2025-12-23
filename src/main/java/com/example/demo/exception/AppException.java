// src/main/java/com/example/demo/exception/AppException.java
package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    @Override public String getMessage() { return message; }
}