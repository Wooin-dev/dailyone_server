package com.wooin.dailyone.dto;

import com.wooin.dailyone.controller.request.GoalCreateRequest;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.Goal}
 */
public record GoalDto(
        Long id,
        String originalGoal,
        String simpleGoal,
        String motivationComment,
        String congratsComment,
        UserDto user,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy

) {

    public static GoalDto from(GoalCreateRequest request) {
        return of(null,
                request.getOriginalGoal(),
                request.getSimpleGoal(),
                request.getMotivationComment(),
                request.getCongratsComment(),
                null,
                null,
                null,
                null,
                null);
    }


    public static GoalDto of(Long id,
                             String originalGoal,
                             String simpleGoal,
                             String motivationComment,
                             String congratsComment,
                             UserDto user,
                             LocalDateTime createdAt,
                             String createdBy,
                             LocalDateTime modifiedAt,
                             String modifiedBy) {
        return new GoalDto(
                id,
                originalGoal,
                simpleGoal,
                motivationComment,
                congratsComment,
                user,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy
        );
    }
}