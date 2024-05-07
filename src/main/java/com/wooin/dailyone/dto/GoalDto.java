package com.wooin.dailyone.dto;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import com.wooin.dailyone.model.Goal;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.Goal}
 */
@Builder
public record GoalDto(
        Long id,
        String originalGoal,
        String simpleGoal,
        String motivationComment,
        String congratsComment,
        UserDto user,
        boolean isDoneToday,
        int doneCount,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy

) {

    public static GoalDto fromRequest(GoalCreateRequest request) {
        return GoalDto.builder()
                .originalGoal(request.getOriginalGoal())
                .simpleGoal(request.getSimpleGoal())
                .motivationComment(request.getMotivationComment())
                .congratsComment(request.getCongratsComment())
                .build();
    }

    public static GoalDto fromEntity(Goal goal, boolean isDoneToday, int doneCount) {
        return builderFromEntity(goal)
                .isDoneToday(isDoneToday)
                .doneCount(doneCount)
                .build();

    }

    public static GoalDto fromEntity(Goal goal) {
        return builderFromEntity(goal)
                .build();
    }

    private static GoalDtoBuilder builderFromEntity(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .originalGoal(goal.getOriginalGoal())
                .simpleGoal(goal.getSimpleGoal())
                .motivationComment(goal.getMotivationComment())
                .congratsComment(goal.getCongratsComment())
                .createdAt(goal.getCreatedAt())
                .modifiedAt(goal.getModifiedAt());
    }
}