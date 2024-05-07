package com.wooin.dailyone.controller.response;

import com.wooin.dailyone.dto.DoneDto;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class DoneOfMonthArrayResponse {

    private String[] doneOfMonthArray;

    public DoneOfMonthArrayResponse(List<DoneDto> doneDtos) {

        Set<String> doneOfMonthSet = new HashSet<>();

        for (DoneDto doneDto : doneDtos) {
            doneOfMonthSet.add(doneDto.createdAt().toString().substring(0, 10));
        }
        this.doneOfMonthArray = doneOfMonthSet.toArray(new String[0]); //배열의 크기를 0으로 하면 자동으로 설정해준다.
    }
}
