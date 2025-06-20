package com.fairrummy.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

public class BadRequestRuntimeException extends RuntimeException {
    @Getter
    private final int statusCode;
    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;

    public BadRequestRuntimeException(int statusCode, HttpStatus httpStatus, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BadRequestRuntimeException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(t);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
