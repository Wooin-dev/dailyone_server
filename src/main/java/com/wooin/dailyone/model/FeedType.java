package com.wooin.dailyone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedType {

    DO_DONE("{fromUserNickname}님의 DONE!"),
    DO_SUPER_DONE("{fromUserNickname}님의  SUPER-DONE!"),
    ;


    private final String feedText;
}
