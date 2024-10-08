package com.wooin.dailyone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //User
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email is duplicated"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "Email is not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Invalid Permission"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),

    //GOAL
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "Goal is not found"),

    //PROMISE-GOAL
    PROMISE_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "Promise Goal is not found"),
    PROMISE_GOAL_NOT_FINISHED(HttpStatus.CONFLICT, "Promise Goal has not enough Done-Count to finish it"),
    PROMISE_GOAL_ALREADY_EXIST(HttpStatus.CONFLICT, "Promise Goal has already exist"),

    //DONE
    DONE_NOT_FOUND(HttpStatus.NOT_FOUND, "Done is not found"),
    ALREADY_DONE(HttpStatus.CONFLICT, "DONE is already exist"),

    //SERVER
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many Requests at once"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    ;
    private HttpStatus status;
    private String message;
}
