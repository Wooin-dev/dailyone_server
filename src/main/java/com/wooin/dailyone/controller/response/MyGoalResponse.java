package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.model.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyGoalResponse {

    private String originalGoal;
    private String simpleGoal;
    private LocalDateTime createdAt;
    private Integer doneCount;

    public static MyGoalResponse from(Goal goal) {
        return new MyGoalResponse(goal.getOriginalGoal(),
                goal.getSimpleGoal(),
                goal.getCreatedAt(),
                1 //TODO: done count 구현
        );
    }

}
