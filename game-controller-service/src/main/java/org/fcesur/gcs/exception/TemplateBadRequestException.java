package org.fcesur.gcs.exception;

import org.springframework.http.HttpStatus;

public class TemplateBadRequestException extends GeneralRunTimeException {
    public TemplateBadRequestException(int statusCode, HttpStatus httpStatus, String message) {
        super(statusCode, httpStatus, message);
    }

    public TemplateBadRequestException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(statusCode, httpStatus, message, t);
    }
}
