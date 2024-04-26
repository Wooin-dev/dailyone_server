package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.request.UserJoinRequest;
import com.wooin.dailyone.controller.request.UserLoginRequest;
import com.wooin.dailyone.controller.request.UserMyInfoUpdateRequest;
import com.wooin.dailyone.dto.UserDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // 컨트롤러 테스트에 사용되는 어노테이션 중 하나
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    //기존에 배웠던 jackson의 빈이다.
    //객체를 json형태로 직렬화/역직렬화 알아서 해주는데, 해당처럼 가져와서 사용가능한것으로 보인다.

    @MockBean //모킹을 위해서 붙여주는 어노테이션. 아래 mockito의 when. thenReturn등의 메소드를 활용 가능
    private UserService userService;


    @Test
    public void 회원가입() throws Exception {
        ////Given
        String email = "email@email.com";
        String password = "pass12#$";
        String nickname = "wooin";

        ////WHEN
        when(userService.join(email, password, nickname)).thenReturn(mock(UserDto.class)); //TOSTUDY : mock() 메소드?

        ////THEN
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(email, password, nickname)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_이메일로_회원가입하는경우() throws Exception {
        ////Given
        String email = "email@email.com";
        String password = "pass12#$";
        String nickname = "wooin";

        ////WHEN
        when(userService.join(email, password, nickname)).thenThrow(new DailyoneException(ErrorCode.DUPLICATED_EMAIL, ""));

        ////THEN
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(email, password, nickname)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        ////Given
        String email = "email@email.com";
        String password = "pass12#$";

        ////WHEN
        when(userService.login(email, password)).thenReturn("test_token");

        ////THEN
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입되지_않은_이메일로_로그인시도시_에러반환() throws Exception {
        ////Given
        String email = "email@email.com";
        String password = "pass12#$";

        ////WHEN
        when(userService.login(email, password)).thenThrow(new DailyoneException(ErrorCode.EMAIL_NOT_FOUND));

        ////THEN
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isNotFound()); //404
    }
    @Test
    public void 로그인시_틀린패스워드를_입력시_에러반환() throws Exception {
        ////Given
        String email = "email@email.com";
        String password = "pass12#$";

        ////WHEN
        when(userService.login(email, password)).thenThrow(new DailyoneException(ErrorCode.INVALID_PASSWORD));

        ////THEN
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized()); //401
    }

    @Test
    @WithMockUser
    public void 마이페이지에서_내정보_가져오기_성공() throws Exception {
        //mocking
        when(userService.loadUserByEmail(any())).thenReturn(mock(UserDto.class));

        //THEN
        mockMvc.perform(get("/api/v1/users/myinfo")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    @WithAnonymousUser
    public void 마이페이지에서_내정보_가져오기_실패() throws Exception {
        //mocking
        when(userService.loadUserByEmail(any())).thenThrow(new DailyoneException(ErrorCode.INVALID_TOKEN));

        //THEN
        mockMvc.perform(get("/api/v1/users/myinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    public void 마이페이지에서_내정보_수정_성공() throws Exception {
        //GIVEN
        String newNickname = "newNick";

        //THEN
        mockMvc.perform(put("/api/v1/users/myinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserMyInfoUpdateRequest(newNickname)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 마이페이지에서_내정보_수정_로그인정보없을때_실패() throws Exception {
        //GIVEN
        String newNickname = "newNick";

        //THEN
        mockMvc.perform(put("/api/v1/users/myinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserMyInfoUpdateRequest(newNickname)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

}