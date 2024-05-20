package com.wooin.dailyone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.User;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record DoneDto(
        Long doneId,
        @JsonIgnore
        PromiseGoalDto promiseGoalDto,
        Timestamp createdAt,
        Timestamp modifiedAt
) {
    public static DoneDto fromEntity(Done done) {
        return new DoneDto(
                done.getId(),
                PromiseGoalDto.fromEntity(done.getPromiseGoal()),
                done.getCreatedAt(),
                done.getModifiedAt()
        );
    }

    public Done toEntity(User user, PromiseGoal promiseGoal) {
        return Done.builder()
                .id(doneId)
                .promiseGoal(promiseGoal)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
