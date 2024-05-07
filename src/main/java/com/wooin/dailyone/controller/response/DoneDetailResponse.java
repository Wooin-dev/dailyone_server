package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.DoneDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DoneDetailResponse {

    private List<DoneDto> doneList;


}
