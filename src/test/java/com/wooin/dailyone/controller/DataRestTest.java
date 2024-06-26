package com.wooin.dailyone.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled // 2024-04-17) 아래 API들은 Data REST를 통해 구현된 api를 공부 목적으로 확인해본 것. 확인 마친 후 Disabled 추가.
@DisplayName("Data REST - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] Goal 리스트 조회")
    @Test
    void givenNothing_whenRequestingGoals_thenReturnsGoalsJsonResponse() throws Exception {
        ////Given

        ////WHEN////THEN
        mvc.perform(get("/api/goals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] Goal 리스트 단건 조회")
    @Test
    void givenNothing_whenRequestingGoal_thenReturnsGoalJsonResponse() throws Exception {
        ////Given

        ////WHEN////THEN
        mvc.perform(get("/api/goals/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }


}
