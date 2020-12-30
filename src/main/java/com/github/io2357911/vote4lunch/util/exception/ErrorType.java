package com.github.io2357911.vote4lunch.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR(HttpStatus.CONFLICT),
    VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY),
    WRONG_REQUEST(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    ErrorType(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}