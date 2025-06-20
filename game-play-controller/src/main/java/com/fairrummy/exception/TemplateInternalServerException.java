package com.fairrummy.exception;

import org.springframework.http.HttpStatus;

public class TemplateInternalServerException extends GeneralRunTimeException {
    public TemplateInternalServerException(int statusCode, HttpStatus httpStatus, String message) {
        super(statusCode, httpStatus, message);
    }

    public TemplateInternalServerException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(statusCode, httpStatus, message, t);
    }
}