package com.wooin.dailyone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wooin.dailyone.model.Goal;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(value = {"userDto", "goalDto"})
@Builder
public record PromiseGoalDto(
        Long promiseGoalId,
        UserDto userDto,
        GoalDto goalDto,
        LocalDate startDate,
        LocalDate endDate,
        Integer promiseDoneCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt) {

    public static PromiseGoalDto fromEntity(PromiseGoal promiseGoal) {
        return PromiseGoalDto.builder()
                .promiseGoalId(promiseGoal.getId())
                .userDto(UserDto.fromEntity(promiseGoal.getUser()))
                .goalDto(GoalDto.fromEntity(promiseGoal.getGoal()))
                .startDate(promiseGoal.getStartDate())
                .endDate(promiseGoal.getEndDate())
                .promiseDoneCount(promiseGoal.getPromiseDoneCount())
                .createdAt(promiseGoal.getCreatedAt())
                .modifiedAt(promiseGoal.getModifiedAt())
                .build();
    }

    public PromiseGoal toEntity(User user, Goal goal) {
        return PromiseGoal.builder()
                .user(user)
                .goal(goal)
                .startDate(startDate.atStartOfDay())
                .endDate(endDate.atStartOfDay())
                .promiseDoneCount(promiseDoneCount)
                .build();
    }
}
