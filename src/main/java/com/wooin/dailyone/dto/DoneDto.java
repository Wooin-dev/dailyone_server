package com.wooin.dailyone.dto;

import com.wooin.dailyone.model.Done;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public record DoneDto(
        Long doneId,
        Timestamp createdAt,
        GoalDto goalDto
) {
    public static DoneDto fromEntity(Done done) {
        return new DoneDto(done.getId(), done.getCreatedAt(), GoalDto.fromEntity(done.getGoal()));
    }
}
