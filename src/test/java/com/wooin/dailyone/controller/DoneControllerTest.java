package com.wooin.dailyone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.service.DoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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