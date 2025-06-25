package org.fcesur.gcs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends RuntimeException {
    @Getter
    private final int statusCode;
    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;

    public InternalServerErrorException(int statusCode, HttpStatus httpStatus, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public InternalServerErrorException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(t);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}