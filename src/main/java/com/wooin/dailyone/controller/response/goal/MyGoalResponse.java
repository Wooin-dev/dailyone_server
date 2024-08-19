package com.wooin.dailyone.controller.response.goal;

import com.wooin.dailyone.dto.GoalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyGoalResponse {

    private Long id;
    private String originalGoal;
    private String simpleGoal;
    private LocalDateTime createdAt;
    private Integer doneCount;
    private boolean isDoneToday;

    public static MyGoalResponseBuilder builderFromDto(GoalDto goalDto) {
        return MyGoalResponse.builder()
                .id(goalDto.id())
                .originalGoal(goalDto.originalGoal())
                .simpleGoal(goalDto.simpleGoal())
                .originalGoal(goalDto.originalGoal())
                .createdAt(goalDto.createdAt());
    }

    public static MyGoalResponse fromDto (GoalDto goalDto) {
        return builderFromDto(goalDto).build();
    }
}
