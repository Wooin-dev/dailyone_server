package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserMyInfoResponse {
    private String email;
    private String nickname;
    private LocalDateTime createdAt;

    public UserMyInfoResponse(UserDto userDto) {
        this.email = userDto.email();
        this.nickname = userDto.nickname();
        this.createdAt = userDto.createdAt();
    }
}
