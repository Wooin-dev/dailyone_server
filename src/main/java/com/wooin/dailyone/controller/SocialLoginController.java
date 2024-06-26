package com.wooin.dailyone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooin.dailyone.controller.response.Response;
import com.wooin.dailyone.controller.response.user.UserLoginResponse;
import com.wooin.dailyone.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/social-login")
@RequiredArgsConstructor
@RestController
public class SocialLoginController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao/callback") //버튼을 누르게 되면 카카오 서버로부터 리다이렉트되어 인가 코드를 전달받게됨. 해당 URL은 카카오 로그인 홈페이지에서 등록해뒀음.
    public Response<UserLoginResponse> kakaoLogin(@RequestParam String code) throws JsonProcessingException {

        UserLoginResponse userLoginResponse = kakaoLoginService.kakaoLogin(code);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); //check 토큰은 헤더에 넣어서 전달하는 방식.
        return Response.success(userLoginResponse);
    }
}
