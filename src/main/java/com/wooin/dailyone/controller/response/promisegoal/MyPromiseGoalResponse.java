package com.wooin.dailyone.controller.response.promisegoal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.dto.PromiseGoalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPromiseGoalResponse {

    @JsonProperty("goal")           private GoalDto goalDto;
    @JsonProperty("promiseGoal")    private PromiseGoalDto promiseGoalDto;
                                    private Integer doneCount;
                                    private Boolean isDoneToday;
                                    private Integer superDoneCount;
                                    private Boolean isSuperDoneToday;
}
