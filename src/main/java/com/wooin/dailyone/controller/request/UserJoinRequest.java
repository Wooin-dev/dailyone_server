package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 해당클래스의 역할, 폴더 구조 위치는 모두 다르다.
 * 우선 강의해주는 선배 개발자의 방식을 따라가며, 어떠한 장점에 있어 이렇게 정립해왔는지 과정을 추측해보자.
 */
@Getter
@AllArgsConstructor
public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;
}
