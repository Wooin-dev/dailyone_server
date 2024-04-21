package com.wooin.dailyone.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 지네릭에 해당하는 ~~Response 클래스를 담아준다.
 * 응답을 규격화하여 주는 것이 프론트 입장에서 작업할 때 안정성, (안정감?)을  줄 수 있다.
 */
@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T resultReason;

    private static final String RESULT_CODE_SUCCESS = "SUCCESS";
    public static Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(RESULT_CODE_SUCCESS, result);
    }
}
