package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@AllArgsConstructor
public class GoalCreateRequest {

    private String originalGoal;
    private String simpleGoal;
    private String motivationComment;
    private String congratsComment;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer promiseDoneCount;

}
