package com.wooin.dailyone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wooin.dailyone.model.Done;
import com.wooin.dailyone.model.PromiseGoal;
import com.wooin.dailyone.model.SuperDone;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record SuperDoneDto(
        Long superDoneId,
        @JsonIgnore
        PromiseGoalDto promiseGoalDto,
        Timestamp createdAt,
        Timestamp modifiedAt
) {
    public static SuperDoneDto fromEntity(SuperDone superDone) {
        return new SuperDoneDto(
                superDone.getId(),
                PromiseGoalDto.fromEntity(superDone.getPromiseGoal()),
                superDone.getCreatedAt(),
                superDone.getModifiedAt()
        );
    }

    public Done toEntity(PromiseGoal promiseGoal) {
        return Done.builder()
                .id(superDoneId)
                .promiseGoal(promiseGoal)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
