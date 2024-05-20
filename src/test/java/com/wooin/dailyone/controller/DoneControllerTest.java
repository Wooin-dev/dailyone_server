package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.exception.DailyoneException;
import com.wooin.dailyone.exception.ErrorCode;
import com.wooin.dailyone.service.DoneService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoneService doneService;

    @MockBean
    private PromiseGoalService promiseGoalService;


    @Test
    @WithMockUser
    void DONE_내목표에_오늘DONE처리_성공() throws Exception {
        //WHEN//THEN
        mockMvc.perform(post("/api/v1/done/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void DONE_내목표에_오늘DONE처리_로그인하지않은경우_실패() throws Exception {
        //WHEN//THEN
        mockMvc.perform(post("/api/v1/done/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void DONE_내목표가_없는경우_실패() throws Exception {
        //MOCKING
        doThrow(new DailyoneException(ErrorCode.PROMISE_GOAL_NOT_FOUND)).when(doneService).createDone(any(), any());

        //WHEN//THEN
        mockMvc.perform(post("/api/v1/done/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void Done정보_가져오기() throws Exception {

        //GIVEN
        String url = "/api/v1/done/date" + "?createdAt=2024-05-06T15:00:00.000Z";

        //Mocking

        //THEN
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());

    }

}