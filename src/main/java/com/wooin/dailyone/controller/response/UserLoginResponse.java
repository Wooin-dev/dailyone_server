package com.wooin.dailyone.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token; //로그인 완료시 JWT token값만 반환하면 된다.
}
