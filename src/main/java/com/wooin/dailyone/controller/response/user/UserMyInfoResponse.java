package com.wooin.dailyone.controller.response.user;

import com.wooin.dailyone.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserMyInfoResponse {
    private String email;
    private String nickname;
    private Long kakaoId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserMyInfoResponse(UserDto userDto) {
        this.email = userDto.email();
        this.nickname = userDto.nickname();
        this.kakaoId = userDto.kakaoId();
        this.createdAt = userDto.createdAt();
        this.modifiedAt = userDto.modifiedAt();
    }

    public static UserMyInfoResponse fromDto(UserDto userDto) {
        return new UserMyInfoResponse(
                userDto.email(),
                userDto.nickname(),
                userDto.kakaoId(),
                userDto.createdAt(),
                userDto.modifiedAt()
        );
    }
}
