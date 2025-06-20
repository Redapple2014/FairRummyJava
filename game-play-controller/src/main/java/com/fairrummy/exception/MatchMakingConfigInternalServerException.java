package com.fairrummy.exception;

import org.springframework.http.HttpStatus;

public class MatchMakingConfigInternalServerException extends GeneralRunTimeException {
    public MatchMakingConfigInternalServerException(int statusCode, HttpStatus httpStatus, String message) {
        super(statusCode, httpStatus, message);
    }

    public MatchMakingConfigInternalServerException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(statusCode, httpStatus, message, t);
    }
}