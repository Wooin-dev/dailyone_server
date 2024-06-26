package com.wooin.dailyone.controller.response.done;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoneCountResponse {
    private Long promiseGoalId;
    private Integer doneCount;
}
