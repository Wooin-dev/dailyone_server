package com.wooin.dailyone.controller.response.done;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wooin.dailyone.dto.DoneDto;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.dto.PromiseGoalDto;
import lombok.Getter;

@Getter
public class DoneDetailResponse {
    @JsonProperty("done")
    private DoneDto doneDto;
    @JsonProperty("promiseGoal")
    private PromiseGoalDto promiseGoalDto;
    @JsonProperty("goal")
    private GoalDto goalDto;

    public DoneDetailResponse(DoneDto doneDto) {
        this.doneDto = doneDto;
        this.promiseGoalDto = doneDto.promiseGoalDto();
        this.goalDto = doneDto.promiseGoalDto().goalDto();
    }
}