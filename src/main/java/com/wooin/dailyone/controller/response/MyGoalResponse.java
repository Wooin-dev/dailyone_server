package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.GoalDto;
import com.wooin.dailyone.model.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyGoalResponse {

    private Long id;
    private String originalGoal;
    private String simpleGoal;
    private LocalDateTime createdAt;
    private Integer doneCount;
    private boolean isDoneToday;

    public static MyGoalResponse from(GoalDto goalDto) {
        return new MyGoalResponse(
                goalDto.id(),
                goalDto.originalGoal(),
                goalDto.simpleGoal(),
                goalDto.createdAt(),
                goalDto.doneCount(),
                goalDto.isDoneToday()
        );
    }

}
