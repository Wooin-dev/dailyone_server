package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 현재 프로젝트에는 controller 패키지에 request와 response 패키지를 추가로 만들어,
 * 요청과 응답시에만 데이터를 전달해주는 역할의 클래스들을 모으고 있다.
 * dto패키지 내에 클래스들은 서버 내의 계층 이동간에만 활용된다.
 */
@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Long id;
    private String email;
    private String nickname;
    private UserRole role;

    public static UserJoinResponse from(UserDto user) {
        return new UserJoinResponse(
                user.id(),
                user.email(),
                user.nickname(),
                user.role());
    }
}
