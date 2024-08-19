package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GoalFollowRequest {

    private Long goalId;
    private LocalDateTime startDate;
}
