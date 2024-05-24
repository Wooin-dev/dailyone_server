package com.wooin.dailyone.controller.response.superdone;

import com.wooin.dailyone.dto.SuperDoneDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SuperDoneDetailListResponse {

    private List<SuperDoneDetailResponse> superDoneList;

    public static SuperDoneDetailListResponse of(List<SuperDoneDto> superDoneDtoList) {
        return new SuperDoneDetailListResponse(
                superDoneDtoList.stream().map(SuperDoneDetailResponse::new).toList()
        );
    }
}