package com.wooin.dailyone.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedListOfGoalResponse {

    List<FeedOfGoalResponse> feedOfGoalList;

}
