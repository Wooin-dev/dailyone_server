package com.wooin.dailyone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email is duplicated"),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "Email is not joined"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is incorrect"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    ;

    private HttpStatus status;
    private String message;
}
