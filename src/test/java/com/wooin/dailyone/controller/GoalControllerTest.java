package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.controller.request.UserJoinRequest;
import com.wooin.dailyone.service.GoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private final String originalGoal = "푸시업 매일 10개";
    private final String simpleGoal = "푸시업 매일 1개";
    private final String motivationComment = "화이팅";
    private final String congratsComment = "축하";

    @Test
    @WithMockUser //로그인 상태 가정을 위한 유저모킹
    void 목표_생성() throws Exception {
        ////THEN
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser //로그인하지 않은경우 filter에 의해 막히게 된다
    void 목표_생성시_로그인하지않은경우() throws Exception {
        ////THEN
        mockMvc.perform(post("/api/v1/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser //로그인 상태 가정을 위한 유저모킹
    void 내목표_조회() throws Exception {
        //TODO mocking
        ////THEN
        mockMvc.perform(get("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser //로그인하지 않은경우 filter에 의해 막히게 된다
    void 내목표_조회시_로그인하지않은경우() throws Exception {
        //TODO mocking
        ////THEN
        mockMvc.perform(get("/api/v1/goals/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new GoalCreateRequest(originalGoal, simpleGoal, motivationComment, congratsComment)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
