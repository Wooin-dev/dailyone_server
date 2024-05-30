package com.wooin.dailyone.controller.response.superdone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.dto.PromiseGoalDto;
import com.wooin.dailyone.dto.SuperDoneDto;
import lombok.Getter;

@Getter
public class SuperDoneDetailResponse {
    @JsonProperty("superDone")
    private SuperDoneDto superDoneDto;
    @JsonProperty("promiseGoal")
    private PromiseGoalDto promiseGoalDto;
    @JsonProperty("goal")
    private GoalDto goalDto;

    public SuperDoneDetailResponse(SuperDoneDto superDoneDto) {
        this.superDoneDto = superDoneDto;
        this.promiseGoalDto = superDoneDto.promiseGoalDto();
        this.goalDto = superDoneDto.promiseGoalDto().goalDto();
    }
}