package com.fairrummy.exception;

import org.springframework.http.HttpStatus;

public class MatchMakingConfigBadRequestException extends GeneralRunTimeException {
    public MatchMakingConfigBadRequestException(int statusCode, HttpStatus httpStatus, String message) {
        super(statusCode, httpStatus, message);
    }

    public MatchMakingConfigBadRequestException(int statusCode, HttpStatus httpStatus, String message, Throwable t) {
        super(statusCode, httpStatus, message, t);
    }
}
