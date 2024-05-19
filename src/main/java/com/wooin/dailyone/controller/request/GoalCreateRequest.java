package com.wooin.dailyone.controller.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
//@AllArgsConstructor
public class GoalCreateRequest {

    private String originalGoal;
    private String simpleGoal;
    private String motivationComment;
    private String congratsComment;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer promiseDoneCount;

    public GoalCreateRequest(String originalGoal, String simpleGoal, String motivationComment, String congratsComment, LocalDateTime startDate, LocalDateTime endDate, Integer promiseDoneCount) {
        this.originalGoal = originalGoal;
        this.simpleGoal = simpleGoal;
        this.motivationComment = motivationComment;
        this.congratsComment = congratsComment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promiseDoneCount = promiseDoneCount;
        log.debug("startDate = " + startDate);
        log.debug("endDate = " + endDate);
    }
}
