package com.fairrummy.constants;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCodes {
    TEMPLATE_NOT_FOUND(NOT_FOUND.value(), "Template not found", NOT_FOUND),
    ALREADY_IN_DRAFT(BAD_REQUEST.value(), "Template already in draft state", BAD_REQUEST),
    ALREADY_IN_ACTIVE(BAD_REQUEST.value(), "Template already in active state", BAD_REQUEST),
    DUPLICATE_MATCHMAKING_CONFIG(BAD_REQUEST.value(), "MM Config already exists with similar data", BAD_REQUEST),
    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR.value(), "Unknown Error", INTERNAL_SERVER_ERROR),
    MATCH_MAKING_CONFIG_NOT_FOUND(NOT_FOUND.value(), "Match Making Config not found", NOT_FOUND),
    INVALID_TEMPLATE_IDS_FOR_MATCH_MAKING_CONFIG(
            BAD_REQUEST.value(), "Invalid Template Ids for Matchmaking Config", BAD_REQUEST),
    DUPLICATE_MATCHMAKING_CONFIG_FOR_TEMPLATE(
            BAD_REQUEST.value(),
            " Matchmaking Config Already there for the selected template",
            BAD_REQUEST),
    JSON_PROCESSING_ERROR(INTERNAL_SERVER_ERROR.value(), "Unknown Error", INTERNAL_SERVER_ERROR);

    private Integer errorCode;
    private String message;
    private HttpStatus httpStatus;

    ErrorCodes(int errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
