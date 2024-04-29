package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalCreateRequest {

    private String originalGoal;
    private String simpleGoal;
    private String motivationComment;
    private String congratsComment;

}
