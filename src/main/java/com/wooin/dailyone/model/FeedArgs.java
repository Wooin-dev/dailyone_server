package com.wooin.dailyone.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedArgs {
    //User (user who makes events)
    private Long fromUserId;
    private String fromUserNickname;

    //PromiseGoal
    private Long promiseGoalId;
}
