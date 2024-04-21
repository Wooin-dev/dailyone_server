package com.wooin.dailyone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email is duplicated"),
    NOT_JOINED_EMAIL(HttpStatus.NOT_FOUND, "Email is not joined"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is incorrect"),
    ;

    private HttpStatus status;
    private String message;
}
