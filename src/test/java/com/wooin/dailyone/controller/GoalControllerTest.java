package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.response.goal.MyGoalListResponse;
import com.wooin.dailyone.custom.annotaion.withmockuserdto.WithMockUserDto;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.service.GoalService;
import com.wooin.dailyone.service.PromiseGoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    @MockBean
    PromiseGoalService promiseGoalService;

    private final String originalGoal = "푸시업 매일 10개";
    private final String simpleGoal = "푸시업 매일 1개";
    private final String motivationComment = "화이팅";
    private final String congratsComment = "축하";
    private final LocalDateTime startDate = LocalDateTime.now();
    private final LocalDateTime endDate = LocalDateTime.now();

    @Test
    @WithMockUserDto
        //로그인 상태 가정을 위한 유저모킹
    void 목표_생성() throws Exception {
        ////THEN
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment, startDate, endDate, 1)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
        //로그인하지 않은경우 filter에 의해 막히게 된다
    void 목표_생성시_로그인하지않은경우() throws Exception {
        ////THEN
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment, startDate, endDate, 1)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
        //로그인 상태 가정을 위한 유저모킹
    void 내목표_조회() throws Exception {
        //mocking
        when(goalService.selectMyGoal(any())).thenReturn(mock(MyGoalListResponse.class));

        ////THEN
        mockMvc.perform(get("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment, startDate, endDate, 1)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
        //로그인하지 않은경우를 가정. filter에 의해 막히게 된다
    void 내목표_조회시_로그인하지않은경우() throws Exception {
        ////THEN
        mockMvc.perform(get("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment, startDate, endDate, 1)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 내목표_삭제_성공() throws Exception {
        //THEN
        mockMvc.perform(delete("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 내목표_삭제시_로그인하지_않은경우_실패() throws Exception {
        //THEN
        mockMvc.perform(delete("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 내목표_삭제시_다른계정의_삭제요청인경우_실패() throws Exception {
        //MOCKING
        doThrow(new DailyoneException(ErrorCode.INVALID_PERMISSION)).when(goalService).deleteGoal(any());
        //THEN
        mockMvc.perform(delete("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 내목표_삭제시_목표가존재하지않는경우_실패() throws Exception {
        //MOCKING
        doThrow(new DailyoneException(ErrorCode.GOAL_NOT_FOUND)).when(goalService).deleteGoal(any());
        //THEN
        mockMvc.perform(delete("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

}
