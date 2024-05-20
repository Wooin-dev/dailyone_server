package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.request.PromiseGoalCreateRequest;
import com.wooin.dailyone.service.PromiseGoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PromiseGoalControllerTest {

    private final String BASE_URI = "/api/v1/promise-goals";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PromiseGoalService promiseGoalService;

    @Test
    @WithMockUser
        //로그인 상태 가정을 위한 유저모킹
    void 특정_목표를_내_목표로_설정() throws Exception {
        ////THEN
        mockMvc.perform(post(BASE_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(mock(PromiseGoalCreateRequest.class)))
                ).andDo(print())
                .andExpect(status().isOk());
    }


}
