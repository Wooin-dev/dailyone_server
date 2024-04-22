package com.wooin.dailyone.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.wooin.dailyone.model.Goal}
 */
public record GoalDto(
        Long id,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        @NotBlank String originalGoal,
        String simpleGoal,
        String motivationComment,
        String congratsComment,
        UserDto user
) {


    public GoalDto of(Long id,
                   LocalDateTime createdAt,
                   String createdBy,
                   LocalDateTime modifiedAt,
                   String modifiedBy,
                   @NotBlank String originalGoal,
                   String simpleGoal,
                   String motivationComment,
                   String congratsComment,
                   UserDto userDto) {
        return new GoalDto(id,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy,
                originalGoal,
                simpleGoal,
                motivationComment,
                congratsComment,
                userDto);
    }
}