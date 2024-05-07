package com.wooin.dailyone.controller.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DoneDetailRequest {

    private LocalDateTime createdAt;
}
