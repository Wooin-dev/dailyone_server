package com.wooin.dailyone.controller.response.goal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GoalThumbListResponse {
    private List<GoalThumbResponse> goalThumbResponses;
}
