package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 현재로선 UserJoinRequest와 같은 모습이지만, 목적이 다르므로 분리해준다고 한다.
 * 내 생각에는 이렇게 분리를 함으로써 이후 각 기능별 Request시에 변동 사항이 있을 때 유연하게 확장이 가능해보인다.
 * 수정, 확장이 쉬워 보인다는 뜻
 */
@Getter
@AllArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}
