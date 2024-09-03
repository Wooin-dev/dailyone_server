package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.FeedOfGoalDto;
import com.wooin.dailyone.model.FeedArgs;
import com.wooin.dailyone.model.FeedType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedOfGoalResponse {
    //feed
    private Long feedId;
    private FeedType feedType;
    private FeedArgs feedArgs;
    private String feedText;
    private Timestamp createdAt;

    //goal
    private Long goalId;

    //event(done, super-done, ...)

    //user
    private Long userId;
    private String nickname;


    public static FeedOfGoalResponse fromDto(FeedOfGoalDto feedOfGoalDto) {
        return new FeedOfGoalResponse(
                feedOfGoalDto.id(),
                feedOfGoalDto.feedType(),
                feedOfGoalDto.feedArgs(),
                feedOfGoalDto.feedType().getFeedText(),
                feedOfGoalDto.createdAt(),
                feedOfGoalDto.goalDto().id(),
                feedOfGoalDto.promiseGoalDto().userDto().id(),
                feedOfGoalDto.promiseGoalDto().userDto().nickname()
        );
    }
}

