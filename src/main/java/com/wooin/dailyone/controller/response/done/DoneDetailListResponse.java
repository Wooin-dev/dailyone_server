package com.wooin.dailyone.controller.response.done;

import com.wooin.dailyone.dto.DoneDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DoneDetailListResponse {

    private List<DoneDetailResponse> doneList;

    public static DoneDetailListResponse of(List<DoneDto> doneDtoList) {
        return new DoneDetailListResponse(
                doneDtoList.stream().map(DoneDetailResponse::new).toList()
        );
    }
}