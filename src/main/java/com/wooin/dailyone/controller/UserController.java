package com.wooin.dailyone.controller;

import com.wooin.dailyone.controller.request.UserJoinRequest;
import com.wooin.dailyone.controller.request.UserLoginRequest;
import com.wooin.dailyone.controller.request.UserMyInfoUpdateRequest;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.user.UserJoinResponse;
import com.wooin.dailyone.controller.response.user.UserLoginResponse;
import com.wooin.dailyone.controller.response.user.UserMyInfoResponse;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/myinfo")
    public Response<UserMyInfoResponse> getMyInfo(Authentication authentication) {
        UserDto userDto = userService.loadUserByEmail(authentication.getName());
        return Response.success(new UserMyInfoResponse(userDto));
    }

    @PutMapping("/myinfo")
    public Response<UserMyInfoResponse> modifyMyInfo(@RequestBody UserMyInfoUpdateRequest request, Authentication authentication) {
        UserDto userDto = userService.modifyMyInfo(UserDto.fromRequest(request), authentication.getName());
        return Response.success(new UserMyInfoResponse(userDto));
    }


}
