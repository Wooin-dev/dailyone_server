package com.wooin.dailyone.controller.response.superdone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuperDoneCountResponse {
    private Long promiseGoalId;
    private Integer superDoneCount;
}
