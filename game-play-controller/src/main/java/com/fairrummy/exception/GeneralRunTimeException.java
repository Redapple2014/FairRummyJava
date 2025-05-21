package com.fairrummy.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
public class GeneralRunTimeException extends RuntimeException{
    @Getter private final int statusCode;
    @Getter private final String message;
    @Getter private final HttpStatus httpStatus;

    public GeneralRunTimeException(int statusCode, HttpStatus httpStatus, String message)
    {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public GeneralRunTimeException(int statusCode, HttpStatus httpStatus, String message, Throwable t)
    {
        super(t);
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
