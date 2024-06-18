package com.wooin.dailyone.controller.response.promisegoal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyPromiseGoalListResponse {
    List<MyPromiseGoalResponse> myPromiseGoalResponseList;
}
