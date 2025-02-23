package com.golden.filme_app.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ApiErrorResponse {
    private String message;
    private int status;
    private String error;

    public ApiErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }
}