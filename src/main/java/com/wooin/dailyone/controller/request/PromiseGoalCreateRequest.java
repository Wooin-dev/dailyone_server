package com.wooin.dailyone.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PromiseGoalCreateRequest {
    private Long goalId;
    private Integer promiseDoneCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
