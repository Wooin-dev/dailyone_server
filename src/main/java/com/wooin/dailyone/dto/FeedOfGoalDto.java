package com.wooin.dailyone.dto;

import com.wooin.dailyone.model.*;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record FeedOfGoalDto(
        Long id,
        GoalDto goalDto,
        PromiseGoalDto promiseGoalDto,
        FeedType feedType,
        FeedArgs feedArgs,
        Timestamp createdAt,
        Timestamp modifiedAt
) {

    public static FeedOfGoalDto fromEntity(FeedOfGoal feedOfGoal) {
        return new FeedOfGoalDto(
                feedOfGoal.getId(),
                GoalDto.fromEntity(feedOfGoal.getGoal()),
                PromiseGoalDto.fromEntity(feedOfGoal.getPromiseGoal()),
                feedOfGoal.getFeedType(),
                feedOfGoal.getFeedArgs(),
                feedOfGoal.getCreatedAt(),
                feedOfGoal.getModifiedAt()
        );
    }

    public FeedOfGoal toEntity(Goal goal, PromiseGoal promiseGoal){
        return FeedOfGoal.builder()
                .id(id)
                .goal(goal)
                .promiseGoal(promiseGoal)
                .feedType(feedType)
                .feedArgs(feedArgs)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }

}
