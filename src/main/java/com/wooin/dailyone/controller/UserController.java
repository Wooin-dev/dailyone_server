package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.UserJoinRequest;
import com.wooin.dailyone.controller.request.UserLoginRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.UserJoinResponse;
import com.wooin.dailyone.controller.response.UserLoginResponse;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto savedUserDto = userService.join(request.getEmail(), request.getPassword(), request.getNickname());
        return Response.success(UserJoinResponse.from(savedUserDto));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

}
